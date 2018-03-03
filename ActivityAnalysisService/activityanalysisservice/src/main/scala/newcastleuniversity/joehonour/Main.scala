package newcastleuniversity.joehonour

import java.util.Properties

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

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

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val raw_file_source = Source
      .fromFile("../test_json_files/2018-03-02__test_video_one.json")
      .mkString
    val json_result = parse(raw_file_source)
    val parsedResult = json_result.extract[Messages]
    val source = env.fromCollection(parsedResult.ordered_messages)

    val count = source
      .flatMap { _.detected_objects }
        .map { obj => (obj.uuid, 1)}
        .keyBy { _._1 }
        .sum(1)

    count.print()

    env.execute("Test flink job")

    //env.addSource(new FlinkKafkaConsumer081(kafkaTopic, new SimpleStringSchema(), kafkaProperties))

    // run count on how many frames each uuid appeared
    // read and parse file

//
//    val flinkSource = env.fromCollection(parsedResult.ordered_messages)
//    val counts = flinkSource
//      .flatMap { _.detected_objects }
//      .map { v => (v.uuid, 1) }
//      .groupBy(0)
//      .sum(1)
//
//    counts.print()
  }
}
