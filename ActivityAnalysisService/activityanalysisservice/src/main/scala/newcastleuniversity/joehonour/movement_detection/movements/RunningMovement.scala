package newcastleuniversity.joehonour.movement_detection.movements

import newcastleuniversity.joehonour.movement_detection.objects.MovementObject

case class RunningMovement(override val uuid: String,
                           override val fromLocationX: Double,
                           override val fromLocationY: Double,
                           override val toLocationX: Double,
                           override val toLocationY: Double,
                           override val averageDisplacement: Double) extends DetectedMovement
{
  override def movement_type: String = "running"
}

object RunningMovement {

  def buildRunningMovementFrom(movementObjects: Iterable[MovementObject]): RunningMovement
    = MovementBuilderHelper.buildMovementFrom(movementObjects, RunningMovement(_, _, _, _, _, _))
}
