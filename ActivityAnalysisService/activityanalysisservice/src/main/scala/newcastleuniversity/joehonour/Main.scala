package newcastleuniversity.joehonour

import newcastleuniversity.joehonour.messages.Frame
import newcastleuniversity.joehonour.movement_detection.detectors._
import newcastleuniversity.joehonour.input_streams._
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object Main {

  def main(args: Array[String]) {

    //build configuration
    val properties = CommandLineParser.parseCommandLineArguments(args)

    //build data source
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaSource: FlinkKafkaConsumer011[Frame] = kafkaStreamForFrameMessageTopic(properties)

    //run detections
    val sourceOfDetectedObjects = env.addSource(kafkaSource)
        .flatMap { _.detected_objects }

    walkingDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .print()

    env.execute("detection-walking-task")
  }
}
