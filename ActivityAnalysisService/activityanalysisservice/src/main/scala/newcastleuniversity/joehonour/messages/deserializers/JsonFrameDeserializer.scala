package newcastleuniversity.joehonour.messages.deserializers

import newcastleuniversity.joehonour.messages.Frame
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse


class JsonFrameDeserializer extends AbstractDeserializationSchema[Frame] {

  override def deserialize(message: Array[Byte]): Frame = {
    implicit val formats: DefaultFormats.type = DefaultFormats

    val rawMessage = message.map {_.toChar }.mkString
    val messageAsJson = parse(rawMessage)
    messageAsJson.extract[Frame]
  }
}
