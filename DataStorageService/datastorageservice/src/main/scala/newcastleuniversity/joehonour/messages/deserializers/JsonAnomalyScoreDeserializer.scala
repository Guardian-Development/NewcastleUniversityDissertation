package newcastleuniversity.joehonour.messages.deserializers

import newcastleuniversity.joehonour.messages.{AnomalyScore, Frame}
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

class JsonAnomalyScoreDeserializer extends AbstractDeserializationSchema[AnomalyScore] {

  override def deserialize(message: Array[Byte]): AnomalyScore = {
    implicit val formats: DefaultFormats.type = DefaultFormats

    val rawMessage = message.map {_.toChar }.mkString
    val messageAsJson = parse(rawMessage)
    messageAsJson.extract[AnomalyScore]
  }
}
