package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.messages.deserializers.JsonFrameDeserializer
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.scala._
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

case class PositionalObject(uuid: String,
                            x_position: Double,
                            y_position: Double)

object PositionalObjectProducer {
  def positionObjectFor(uuid: String,
                        x_position: Double,
                        y_position: Double,
                        width: Double,
                        height: Double): PositionalObject = {
    val centre_x = x_position + (width / 2)
    val centre_y = y_position + (height / 2)
    PositionalObject(uuid, centre_x, centre_y)
  }
}

case class MovementObject(uuid: String,
                          x_position: Double,
                          y_position: Double,
                          displacement: Double)

class DisplacementAggregator extends AggregateFunction[PositionalObject, MovementObject, MovementObject] {
  override def add(value: PositionalObject, accumulator: MovementObject): MovementObject = {
    accumulator.uuid match {
      case null => MovementObject(value.uuid, value.x_position, value.y_position, 0)
      case _ => calculateVelocityAverage(accumulator, value)
    }
  }

  private def calculateVelocityAverage(currentVelocity: MovementObject,
                                       newPosition: PositionalObject) : MovementObject = {
    val displacement_x = currentVelocity.x_position - newPosition.x_position
    val displacement_y = currentVelocity.y_position - newPosition.y_position
    val distanceMoved = math.sqrt(displacement_x * displacement_x + displacement_y * displacement_y)
    val averageDisplacement = (currentVelocity.displacement + distanceMoved) / 2

    MovementObject(
      newPosition.uuid,
      newPosition.x_position,
      newPosition.y_position,
      averageDisplacement)
  }

  override def createAccumulator(): MovementObject = MovementObject(null, 0, 0, 0)

  override def getResult(accumulator: MovementObject): MovementObject = accumulator

  override def merge(a: MovementObject, b: MovementObject): MovementObject = {
    val average_x = (a.x_position + b.x_position) / 2
    val average_y = (a.y_position + b.y_position) / 2
    val averageVelocity = (a.displacement + b.displacement) / 2

    MovementObject(a.uuid, average_x, average_y, averageVelocity)
  }
}

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

    val peopleInDetectedObjects = sourceOfDetectedObjects
        .filter { detectedObject => detectedObject.`type` == "person" }

    val positionOfPeople = peopleInDetectedObjects
      .map { detected_object => PositionalObjectProducer
          .positionObjectFor(
            detected_object.uuid,
            detected_object.x_position,
            detected_object.y_position,
            detected_object.width,
            detected_object.height)
      }

    val movementCalculationsOfPeoplePattern = positionOfPeople
      .keyBy{ _.uuid }
      .countWindow(10, 2)
      .aggregate(new DisplacementAggregator)

    val walkingDisplacementMin = 0.5
    val walkingDisplacementMax = 1.5
    val walkingPatternDetector = Pattern.begin[MovementObject]("walking-pattern-start")
        .where { person => person.displacement >= walkingDisplacementMin }
        .where { person => person.displacement <= walkingDisplacementMax }
        .timesOrMore(5)
        .consecutive()

    val walkingDetectionStream = CEP.pattern(
      movementCalculationsOfPeoplePattern.keyBy { _.uuid },
      walkingPatternDetector)

    walkingDetectionStream.select(o => o).print()

    env.execute("Test flink job")
  }
}
