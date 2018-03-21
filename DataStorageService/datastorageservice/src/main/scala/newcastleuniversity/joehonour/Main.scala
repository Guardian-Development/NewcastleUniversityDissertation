package newcastleuniversity.joehonour

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}
import org.neo4j.driver.v1.Config

case class TestUser(name: String, lastName: String, age: Int, city: String)

object Main {

  def main(args: Array[String]) {

    val properties = CommandLineParser.parseCommandLineArguments(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val inputStream = env
      .addSource(InputStreams.kafkaStreamForFrameMessageTopic(properties))
      .flatMap { _.detected_objects }

    inputStream.map(o => {
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

    env.execute("data-storage-task")
  }
}