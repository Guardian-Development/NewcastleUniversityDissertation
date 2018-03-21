package joehonour.newcastleuniversity.anomalydetectionservice.messages

import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import scala.collection.mutable

case class MovementObserved(movement_uuid: String,
                            object_uuid: String,
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

  def toVector(m: MovementObserved): linalg.Vector = {
    Vectors.dense(
      movementTypeAsInt(m.movement_type),
      m.from_position_x,
      m.from_position_y,
      m.to_position_x,
      m.to_position_y,
      m.average_displacement)
  }

  private val movementTypeToInt = mutable.Map[String, Int]()
  private var currentTopIndex = 0

  private def movementTypeAsInt(movementType: String) : Int = {
    movementTypeToInt.get(movementType) match {
      case Some(index) => index
      case None =>
        currentTopIndex = currentTopIndex + 1
        movementTypeToInt += (movementType -> currentTopIndex)
        currentTopIndex
    }
  }
}