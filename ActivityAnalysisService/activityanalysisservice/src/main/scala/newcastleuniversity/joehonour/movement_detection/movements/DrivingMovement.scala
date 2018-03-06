package newcastleuniversity.joehonour.movement_detection.movements

import newcastleuniversity.joehonour.movement_detection.objects.MovementObject

case class DrivingMovement(override val uuid: String,
                           override val fromLocationX: Double,
                           override val fromLocationY: Double,
                           override val toLocationX: Double,
                           override val toLocationY: Double,
                           override val averageDisplacement: Double) extends DetectedMovement
{
  override def movement_type: String = "driving"
}

object DrivingMovement {

  def buildDrivingMovementFrom(movementObjects: Iterable[MovementObject]): DrivingMovement
    = MovementBuilderHelper.buildMovementFrom(movementObjects, DrivingMovement(_, _, _, _, _, _))
}
