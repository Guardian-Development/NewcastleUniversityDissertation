package newcastleuniversity.joehonour.message_graph_converters

import newcastleuniversity.joehonour.messages.{ActivityObserved, AnomalyScore, DetectedObject, Frame}

object RelationshipConverter {

  def detectedObjectToFrameRelationship(detectedObject: DetectedObject, frame: Frame) : String = {
    s"""
       |MATCH (a:DetectedObject),(b:Frame)
       |WHERE a.uuid = '${detectedObject.uuid}' AND b.uuid = '${frame.frame_uuid}'
       |MERGE (a)-[r:WITHIN_FRAME]->(b)
        """.stripMargin
  }

  def activityToDetectedObjectRelationship(activity: ActivityObserved) : String = {
    s"""
       |MATCH (a:ActivityObserved),(b:DetectedObject)
       |WHERE a.uuid = '${activity.movement_uuid}' AND b.uuid = '${activity.object_uuid}'
       |MERGE (a)-[r:OBSERVED_FROM]->(b)
        """.stripMargin
  }

  def anomalyToActivityObservedRelationship(anomalyScore: AnomalyScore) : String = {
    s"""
       |MATCH (a:AnomalyScore),(b:ActivityObserved)
       |WHERE a.uuid = '${anomalyScore.uuid}' AND b.uuid = '${anomalyScore.uuid}'
       |MERGE (a)-[r:ANOMALY_SCORE_FROM]->(b)
        """.stripMargin
  }

  def anomalyToClusterRelationship(anomalyScore: AnomalyScore) : String = {
    s"""
       |MATCH (a:AnomalyScore),(c:Cluster)
       |WHERE a.uuid = '${anomalyScore.uuid}' and c.uuid = '${anomalyScore.cluster}'
        MERGE (a)-[r:CLUSTER_DETECTED_IN]->(c)
     """.stripMargin
  }
}
