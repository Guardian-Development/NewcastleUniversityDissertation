package joehonour.newcastleuniversity.anomalydetectionservice.messages

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.{compact, parse}
import org.json4s.jackson.Serialization.write

case class AnomalyScore(uuid: String, score: Double)

object AnomalyScore {

  def toJson(anomalyScore: AnomalyScore) : String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    compact(parse(write(anomalyScore)))
  }
}
