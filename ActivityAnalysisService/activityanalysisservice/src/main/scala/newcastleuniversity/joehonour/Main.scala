package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.messages.deserializers.JsonFrameDeserializer
import newcastleuniversity.joehonour.movement_detection.MovementDetector
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import newcastleuniversity.joehonour.movement_detection.movements.WalkingMovement
import newcastleuniversity.joehonour.movement_detection.objects.MovementObject
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.util.Collector

object Main {


  def collectWalkingActivityResult(movementObjects: collection.Map[String, Iterable[MovementObject]],
                                   movementActivity: Collector[WalkingMovement]): Unit = {
    val objectsPartOfTheActivity = movementObjects("person-detector")
    movementActivity.collect(WalkingMovement.buildWalkingMovementFrom(objectsPartOfTheActivity))
  }

  def main(args: Array[String]) {

    //build configuration
    val configuration = CommandLineParser.parseCommandLineArguments(args)
    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", configuration.kafkaBootStrapServers)
    kafkaProperties.setProperty("group.id", "testGroup")

    //build data source
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaSource = new FlinkKafkaConsumer011(
      configuration.kafkaTopic,
      new JsonFrameDeserializer(),
      kafkaProperties)
    kafkaSource.setStartFromEarliest()

    //run query
    val sourceOfDetectedObjects = env.addSource(kafkaSource)
        .flatMap { _.detected_objects }

    MovementDetector.builder("person-detector")
      .objectTypeIdentifier("person")
      .activityWindow(10, 5)
      .displacementAggregator(new MovementObjectDisplacementAggregator)
      .objectDisplacementIdentifyingRange(1.0, 2.0)
      .activityRepetitionToTrigger(5)
      .buildDetectionStream(sourceOfDetectedObjects)
      .flatSelect{(objects: collection.Map[String, Iterable[MovementObject]], collector: Collector[WalkingMovement]) =>
        val objectsPartOfTheActivity = objects("person-detector")
        collector.collect(WalkingMovement.buildWalkingMovementFrom(objectsPartOfTheActivity))
      }
      .print()

    env.execute("Test flink job")
  }
}
