package newcastleuniversity.joehonour.message_graph_converters

import newcastleuniversity.joehonour.messages.AnomalyScore

object AnomalyScoreConverter {
  def toCreateScript(anomalyScore: AnomalyScore) : String = {
    s"""
       |MERGE (cluster:Cluster { uuid: '${anomalyScore.cluster}' })
       |MERGE (object:AnomalyScore {
       |  uuid:'${anomalyScore.uuid}',
       |  cluster:${anomalyScore.cluster},
       |  score:${anomalyScore.score}})
       |""".stripMargin
  }
}
