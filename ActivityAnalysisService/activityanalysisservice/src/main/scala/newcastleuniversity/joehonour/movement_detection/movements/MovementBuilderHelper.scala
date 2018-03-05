package newcastleuniversity.joehonour.movement_detection.movements

import newcastleuniversity.joehonour.movement_detection.objects.MovementObject

object MovementBuilderHelper {

  def buildMovementFrom[T <: DetectedMovement](movementObjects: Iterable[MovementObject],
                                               builder: (String, Double, Double, Double, Double, Double) => T): T = {
    val uuid = movementObjects.head.uuid
    val averageDisplacement = movementObjects.map { _.displacement }.sum / movementObjects.size
    val fromLocationX = movementObjects.head.xPosition
    val fromLocationY = movementObjects.head.yPosition
    val toLocationX = movementObjects.last.xPosition
    val toLocationY = movementObjects.last.yPosition

    builder(uuid, fromLocationX, fromLocationY, toLocationX, toLocationY, averageDisplacement)
  }
}
