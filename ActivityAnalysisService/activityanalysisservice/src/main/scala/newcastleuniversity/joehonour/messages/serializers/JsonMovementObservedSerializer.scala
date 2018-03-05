package newcastleuniversity.joehonour.messages.serializers

import newcastleuniversity.joehonour.messages.MovementObserved
import org.apache.flink.api.common.serialization.SerializationSchema
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization._

class JsonMovementObservedSerializer extends SerializationSchema[MovementObserved] {

  override def serialize(movement: MovementObserved): Array[Byte] = {
    implicit val formats: DefaultFormats.type = DefaultFormats

    val movementAsJson = compact(parse(write(movement)))
    movementAsJson.getBytes
  }
}
