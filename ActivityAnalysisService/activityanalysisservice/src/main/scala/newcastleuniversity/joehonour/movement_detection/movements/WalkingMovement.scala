package newcastleuniversity.joehonour.movement_detection.movements

import newcastleuniversity.joehonour.movement_detection.objects.MovementObject

case class WalkingMovement(override val uuid: String,
                           override val fromLocationX: Double,
                           override val fromLocationY: Double,
                           override val toLocationX: Double,
                           override val toLocationY: Double,
                           override val averageDisplacement: Double) extends DetectedMovement
{
  override def movement_type: String = "Walking"
}

object WalkingMovement {

  def buildWalkingMovementFrom(movementObjects: Iterable[MovementObject]): WalkingMovement = {
    val uuid = movementObjects.head.uuid
    val averageDisplacement = movementObjects.map { _.displacement }.sum / movementObjects.size
    val fromLocationX = movementObjects.head.xPosition
    val fromLocationY = movementObjects.head.yPosition
    val toLocationX = movementObjects.last.xPosition
    val toLocationY = movementObjects.last.yPosition

    WalkingMovement(uuid, fromLocationX, fromLocationY, toLocationX, toLocationY, averageDisplacement)
  }
}
