package newcastleuniversity.joehonour

import org.apache.flink.api.scala._

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

object WordCount {

  implicit val formats: DefaultFormats.type = DefaultFormats

  def main(args: Array[String]) {

    // read and parse file
    val raw_file_source = Source
        .fromFile("../test_json_files/2018-03-02__test_video_one.json")
        .mkString

    val json_result = parse(raw_file_source)
    val parsedResult = json_result.extract[Messages]


    // run count on how many frames each uuid appeared
    val env = ExecutionEnvironment.getExecutionEnvironment
    val flinkSource = env.fromCollection(parsedResult.ordered_messages)
    val counts = flinkSource
      .flatMap { _.detected_objects }
      .map { v => (v.uuid, 1) }
      .groupBy(0)
      .sum(1)

    counts.print()
  }
}
