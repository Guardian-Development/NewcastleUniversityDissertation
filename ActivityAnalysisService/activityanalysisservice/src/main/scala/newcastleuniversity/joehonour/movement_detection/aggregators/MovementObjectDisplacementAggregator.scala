package newcastleuniversity.joehonour.movement_detection.aggregators

import newcastleuniversity.joehonour.movement_detection.objects.{MovementObject, PositionalObject}
import org.apache.flink.api.common.functions.AggregateFunction

class MovementObjectDisplacementAggregator extends AggregateFunction[PositionalObject, MovementObject, MovementObject] {

  override def add(value: PositionalObject, accumulator: MovementObject): MovementObject = {
    accumulator.uuid match {
      case null => MovementObject(value.uuid, value.x_position, value.y_position, 0)
      case _ => calculateDisplacementAverage(accumulator, value)
    }
  }

  private def calculateDisplacementAverage(currentVelocity: MovementObject,
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
