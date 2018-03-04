package newcastleuniversity.joehonour.movement_detection.objects

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
