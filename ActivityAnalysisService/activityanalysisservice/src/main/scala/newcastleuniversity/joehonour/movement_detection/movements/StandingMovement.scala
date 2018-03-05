package newcastleuniversity.joehonour.movement_detection.movements

import newcastleuniversity.joehonour.movement_detection.objects.MovementObject

case class StandingMovement(override val uuid: String,
                            override val fromLocationX: Double,
                            override val fromLocationY: Double,
                            override val toLocationX: Double,
                            override val toLocationY: Double,
                            override val averageDisplacement: Double) extends DetectedMovement
{
  override def movement_type: String = "standing"
}

object StandingMovement {

  def buildStandingMovementFrom(movementObjects: Iterable[MovementObject]): StandingMovement
    = MovementBuilderHelper.buildMovementFrom(movementObjects, StandingMovement(_, _, _, _, _, _))
}