package newcastleuniversity.joehonour.movement_detection.movements

import newcastleuniversity.joehonour.movement_detection.objects.MovementObject

case class WalkingMovement(override val uuid: String,
                   override val from_location_x: Double,
                   override val from_location_y: Double,
                   override val to_location_x: Double,
                   override val to_location_y: Double,
                   override val average_displacement: Double) extends DetectedMovement
{
  override def movement_type: String = "Walking"
}

object WalkingMovement {

  def buildWalkingMovementFrom(movementObjects: Iterable[MovementObject]): WalkingMovement = {
    val uuid = movementObjects.head.uuid
    val averageDisplacement = movementObjects.map { _.displacement }.sum / movementObjects.size
    val fromLocationX = movementObjects.head.x_position
    val fromLocationY = movementObjects.head.y_position
    val toLocationX = movementObjects.last.x_position
    val toLocationY = movementObjects.last.y_position

    WalkingMovement(uuid, fromLocationX, fromLocationY, toLocationX, toLocationY, averageDisplacement)
  }
}
