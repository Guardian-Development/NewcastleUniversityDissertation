package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.messages.deserializers.JsonFrameDeserializer
import newcastleuniversity.joehonour.movement_detection.MovementDetector
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.streaming.api.scala._

object Main {

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
      .activityWindow(10, 2)
      .displacementAggregator(new MovementObjectDisplacementAggregator)
      .objectDisplacementIdentifyingRange(0.2, 1.0)
      .activityRepetitionToTrigger(5)
      .buildDetectionStream(sourceOfDetectedObjects)
      .flatSelect( (map, collector) => {
        val detections = map("person-detector")
        val uuid = detections.head.uuid
        val averageDisplacement = detections.map { _.displacement }.sum
        //TODO: collect result here!
      })
      .print()

    env.execute("Test flink job")
  }
}
