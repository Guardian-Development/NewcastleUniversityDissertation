package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.input_streams.InputStreams
import newcastleuniversity.joehonour.message_graph_converters._
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.neo4j.driver.v1._

object Main {

  def main(args: Array[String]) {

    val properties = CommandLineParser.parseCommandLineArguments(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val frameInputStream = env
      .addSource(InputStreams.kafkaStreamForFrameMessageTopic(properties))

    frameInputStream.map(f => {
      val (driver, session) = databaseSessionFor(properties)
      val frameCreation = FrameConverter.toCreateScript(f)
      session.run(frameCreation)

      f.detected_objects.foreach(o => {
        val objectCreation = DetectedObjectConverter.toCreateScript(o)
        session.run(objectCreation)
        println(o)

        val objectToFrame = RelationshipConverter.detectedObjectToFrameRelationship(o, f)
        session.run(objectToFrame)
      })

      val activityToObservations = RelationshipConverter.activityToDetectedObjectRelationship()
      session.run(activityToObservations)

      session.close()
      driver.close()
    })

    val activityInputStream = env
        .addSource(InputStreams.kafkaStreamForActivityMessageTopic(properties))

    activityInputStream.map(o => {
      val (driver, session) = databaseSessionFor(properties)
      val creationScript = ActivityObservedConverter.toCreateScript(o)
      println(o)
      session.run(creationScript)

      val activityToObservations = RelationshipConverter.activityToDetectedObjectRelationship()
      session.run(activityToObservations)

      session.close()
      driver.close()
    })

    val anomalyInputStream = env
        .addSource(InputStreams.kafkaStreamForAnomalyMessageTopic(properties))

    anomalyInputStream.map(o => {
      val (driver, session) = databaseSessionFor(properties)
      val creationScript = AnomalyScoreConverter.toCreateScript(o)
      session.run(creationScript)

      println(o)

      val anomalyToActivity = RelationshipConverter.anomalyToActivityObservedRelationship()
      val anomalyToCluster = RelationshipConverter.anomalyToClusterRelationship()
      session.run(anomalyToActivity)
      session.run(anomalyToCluster)

      session.close()
      driver.close()
    })

    env.execute("data-storage-task")
  }

  private def databaseSessionFor(properties: Properties): (Driver, Session) = {
    val config = Config.build.withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig
    val driver = GraphDatabase.driver(
      properties.getProperty("neo4j.connection.url"),
      AuthTokens.basic(
        properties.getProperty("neo4j.database.username"),
        properties.getProperty("neo4j.database.password")),
      config)
    val session = driver.session
    (driver, session)
  }
}