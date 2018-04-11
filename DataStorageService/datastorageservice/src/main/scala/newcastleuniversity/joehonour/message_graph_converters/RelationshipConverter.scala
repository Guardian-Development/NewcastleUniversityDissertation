package newcastleuniversity.joehonour.message_graph_converters

import newcastleuniversity.joehonour.messages.{DetectedObject, Frame}

object RelationshipConverter {

  def detectedObjectToFrameRelationship(detectedObject: DetectedObject, frame: Frame) : String = {
    s"""
       |MATCH (a:DetectedObject),(b:Frame)
       |WHERE a.uuid = '${detectedObject.uuid}' AND b.uuid = '${frame.frame_uuid}'
       |MERGE (a)-[r:WITHIN_FRAME { x_position: '${detectedObject.x_position}', y_position: '${detectedObject.y_position}'}]->(b)
        """.stripMargin
  }

  def activityToDetectedObjectRelationship() : String = {
    s"""
       |MATCH (a:ActivityObserved)
       |WITH a
       |MATCH (b:DetectedObject)
       |where a.object_uuid = b.uuid
       |MERGE (a)-[r:OBSERVED_FROM]->(b)
    """.stripMargin
  }

  def anomalyToActivityObservedRelationship() : String = {
    s"""
       |MATCH (a:AnomalyScore)
       |WITH a
       |MATCH (b:ActivityObserved)
       |where a.uuid = b.uuid
       |MERGE (a)-[r:ANOMALY_SCORE_FROM]->(b)
    """.stripMargin
  }

  def anomalyToClusterRelationship() : String = {
    s"""
       |MATCH (a:AnomalyScore)
       |WITH a
       |MATCH (c:Cluster)
       |where a.cluster = c.uuid
       |MERGE (a)-[r:CLUSTER_DETECTED_IN]->(c)
    """.stripMargin
  }
}
