package newcastleuniversity.joehonour

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010, FlinkKafkaConsumer011}

import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._

case class DetectedObject(`type`: String,
                          uuid: String,
                          y_position: Double,
                          x_position: Double,
                          width: Double,
                          height: Double)

case class Frame(detected_objects: List[DetectedObject])
case class Messages(ordered_messages: List[Frame])

object Main {

  implicit val formats: DefaultFormats.type = DefaultFormats

  def main(args: Array[String]) {

    val configuration = CommandLineParser.parseCommandLineArguments(args)
    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", configuration.kafkaBootStrapServers)
    kafkaProperties.setProperty("group.id", "testGroup")

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaSource = new FlinkKafkaConsumer010[String](configuration.kafkaTopic, new SimpleStringSchema(), kafkaProperties)

    kafkaSource.setStartFromEarliest()
    env.addSource(kafkaSource).print()
    env.execute("Test flink job")

//    val env = StreamExecutionEnvironment.getExecutionEnvironment
//    val raw_file_source = Source
//      .fromFile("../test_json_files/2018-03-02__test_video_one.json")
//      .mkString
//    val json_result = parse(raw_file_source)
//    val parsedResult = json_result.extract[Messages]
//    val source = env.fromCollection(parsedResult.ordered_messages)
//
//    val count = source
//      .flatMap { _.detected_objects }
//        .map { obj => (obj.uuid, 1)}
//        .keyBy { _._1 }
//        .sum(1)
//
//    count.print()
  }
}
