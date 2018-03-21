package newcastleuniversity.joehonour.message_graph_converters

import newcastleuniversity.joehonour.messages.DetectedObject

object DetectedObjectConverter {

  def toCreateScript(detectedObject: DetectedObject) : String = {
    s"""
       |CREATE (object:DetectedObject {
       |  type:'${detectedObject.`type`}',
       |  uuid:'${detectedObject.uuid}',
       |  y_position:${detectedObject.y_position},
       |  x_position:${detectedObject.x_position},
       |  width:${detectedObject.width},
       |  height:${detectedObject.height}})
       |""".stripMargin
  }
}
