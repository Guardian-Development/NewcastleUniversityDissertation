package newcastleuniversity.joehonour

import newcastleuniversity.joehonour.input_streams.InputStreams
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}
import org.neo4j.driver.v1.Config

object Main {

  def main(args: Array[String]) {

    val properties = CommandLineParser.parseCommandLineArguments(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val frameInputStream = env
      .addSource(InputStreams.kafkaStreamForFrameMessageTopic(properties))
      .flatMap { _.detected_objects }

    frameInputStream.map(o => {
      val config = Config.build.withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig
      val driver = GraphDatabase.driver(
        properties.getProperty("neo4j.connection.url"),
        AuthTokens.basic(
          properties.getProperty("neo4j.database.username"),
          properties.getProperty("neo4j.database.password")),
        config)
      val session = driver.session
      val script =
        s"""
           |CREATE (object:DetectedObject {
           |  type:'${o.`type`}',
           |  uuid:'${o.uuid}',
           |  y_position:${o.y_position},
           |  x_position:${o.x_position},
           |  width:${o.width},
           |  height:${o.height}})
           |""".stripMargin
      session.run(script)
      session.close()
      driver.close()
    })

    val activityInputStream = env
        .addSource(InputStreams.kafkaStreamForActivityMessageTopic(properties))

    activityInputStream.map(o => {
      val config = Config.build.withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig
      val driver = GraphDatabase.driver(
        properties.getProperty("neo4j.connection.url"),
        AuthTokens.basic(
          properties.getProperty("neo4j.database.username"),
          properties.getProperty("neo4j.database.password")),
        config)
      val session = driver.session
      val creationScript =
        s"""
           |CREATE (object:ActivityObserved {
           |  uuid:'${o.uuid}',
           |  movement_type:'${o.movement_type}',
           |  from_position_x:${o.from_position_x},
           |  from_position_y:${o.from_position_y},
           |  to_position_x:${o.to_position_x},
           |  to_position_y:${o.to_position_y},
           |  average_displacement:${o.average_displacement}})
           |""".stripMargin
      session.run(creationScript)

      session.close()
      driver.close()
    })

    val anomalyInputStream = env
        .addSource(InputStreams.kafkaStreamForAnomalyMessageTopic(properties))

    anomalyInputStream.map(o => {
      val config = Config.build.withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig
      val driver = GraphDatabase.driver(
        properties.getProperty("neo4j.connection.url"),
        AuthTokens.basic(
          properties.getProperty("neo4j.database.username"),
          properties.getProperty("neo4j.database.password")),
        config)
      val session = driver.session
      val creationScript =
        s"""
           |CREATE (object:AnomalyScore {
           |  uuid:'${o.uuid}',
           |  score:${o.score}})
           |""".stripMargin
      session.run(creationScript)

      val activityToObservations =
        s"""
           |MATCH (a:ActivityObserved),(b:DetectedObject)
           |WHERE a.uuid = '${o.uuid}' AND b.uuid = '${o.uuid}'
           |MERGE (a)-[r:OBSERVED_FROM]->(b)
        """.stripMargin
      session.run(activityToObservations)

      val anomalyToActivity =
        s"""
           |MATCH (a:AnomalyScore),(b:ActivityObserved)
           |WHERE a.uuid = '${o.uuid}' AND b.uuid = '${o.uuid}'
           |MERGE (a)-[r:ANOMALY_SCORE_FROM]->(b)
        """.stripMargin
      session.run(anomalyToActivity)

      session.close()
      driver.close()
    })

    env.execute("data-storage-task")
  }
}