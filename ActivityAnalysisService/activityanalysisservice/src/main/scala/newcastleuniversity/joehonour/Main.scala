package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.messages.deserializers.JsonFrameDeserializer
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object Main {

  def main(args: Array[String]) {

    //build configuration
    val configuration = CommandLineParser.parseCommandLineArguments(args)
    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", configuration.kafkaBootStrapServers)
    kafkaProperties.setProperty("group.id", "testGroup")

    //build data source
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaSource = new FlinkKafkaConsumer011(
      configuration.kafkaTopic,
      new JsonFrameDeserializer(),
      kafkaProperties)
    kafkaSource.setStartFromEarliest()

    //run query
    env.addSource(kafkaSource)
        .flatMap { _.detected_objects }
        .map { obj => (obj.uuid, 1) }
        .keyBy { _._1 }
        .sum(1)
        .print()

    env.execute("Test flink job")
  }
}
