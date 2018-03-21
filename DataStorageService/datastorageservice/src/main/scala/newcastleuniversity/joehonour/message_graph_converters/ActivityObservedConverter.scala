package newcastleuniversity.joehonour.message_graph_converters

import newcastleuniversity.joehonour.messages.ActivityObserved

object ActivityObservedConverter {
  def toCreateScript(activityObserved: ActivityObserved) : String = {
    s"""
       |CREATE (object:ActivityObserved {
       |  uuid:'${activityObserved.movement_uuid}',
       |  movement_type:'${activityObserved.movement_type}',
       |  from_position_x:${activityObserved.from_position_x},
       |  from_position_y:${activityObserved.from_position_y},
       |  to_position_x:${activityObserved.to_position_x},
       |  to_position_y:${activityObserved.to_position_y},
       |  average_displacement:${activityObserved.average_displacement}})
       |""".stripMargin
  }
}
