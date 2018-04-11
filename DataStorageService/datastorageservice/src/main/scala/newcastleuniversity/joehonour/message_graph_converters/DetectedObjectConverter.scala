package newcastleuniversity.joehonour.message_graph_converters

import newcastleuniversity.joehonour.messages.DetectedObject

object DetectedObjectConverter {

  def toCreateScript(detectedObject: DetectedObject) : String = {
    s"""
       |MERGE (n: DetectedObject { uuid: '${detectedObject.uuid}' })
       |set n.type = '${detectedObject.`type`}',
       |    n.y_position = ${detectedObject.y_position},
       |    n.x_position = ${detectedObject.x_position},
       |    n.width = ${detectedObject.width},
       |    n.height = ${detectedObject.height}
     """.stripMargin
  }
}
