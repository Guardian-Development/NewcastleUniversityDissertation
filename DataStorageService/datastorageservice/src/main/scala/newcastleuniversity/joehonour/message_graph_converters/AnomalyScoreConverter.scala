package newcastleuniversity.joehonour.message_graph_converters

import newcastleuniversity.joehonour.messages.AnomalyScore

object AnomalyScoreConverter {
  def toCreateScript(anomalyScore: AnomalyScore) : String = {
    s"""
       |CREATE (object:AnomalyScore {
       |  uuid:'${anomalyScore.uuid}',
       |  score:${anomalyScore.score}})
       |""".stripMargin
  }
}
