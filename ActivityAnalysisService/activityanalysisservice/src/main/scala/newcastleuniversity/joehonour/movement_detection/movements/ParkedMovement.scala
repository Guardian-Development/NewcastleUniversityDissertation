package newcastleuniversity.joehonour.movement_detection.movements

import newcastleuniversity.joehonour.movement_detection.objects.MovementObject

case class ParkedMovement(override val uuid: String,
                           override val fromLocationX: Double,
                           override val fromLocationY: Double,
                           override val toLocationX: Double,
                           override val toLocationY: Double,
                           override val averageDisplacement: Double) extends DetectedMovement
{
  override def movement_type: String = "parked"
}

object ParkedMovement {

  def buildParkedMovementFrom(movementObjects: Iterable[MovementObject]): ParkedMovement
    = MovementBuilderHelper.buildMovementFrom(movementObjects, ParkedMovement(_, _, _, _, _, _))
}