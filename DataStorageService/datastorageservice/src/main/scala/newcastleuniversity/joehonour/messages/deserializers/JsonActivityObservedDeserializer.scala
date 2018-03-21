package newcastleuniversity.joehonour.messages.deserializers

import newcastleuniversity.joehonour.messages.{Frame, ActivityObserved}
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

class JsonActivityObservedDeserializer extends AbstractDeserializationSchema[ActivityObserved] {

  override def deserialize(message: Array[Byte]): ActivityObserved = {
    implicit val formats: DefaultFormats.type = DefaultFormats

    val rawMessage = message.map {_.toChar }.mkString
    val messageAsJson = parse(rawMessage)
    messageAsJson.extract[ActivityObserved]
  }
}
