package joehonour.newcastleuniversity.anomalydetectionservice.messages

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

case class MovementObserved(uuid: String,
                            movement_type: String,
                            from_position_x: Double,
                            from_position_y: Double,
                            to_position_x: Double,
                            to_position_y: Double,
average_displacement: Double)

object MovementObserved {

  def fromJson(json: String) : MovementObserved = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val messageAsJson = parse(json)
    messageAsJson.extract[MovementObserved]
  }
}