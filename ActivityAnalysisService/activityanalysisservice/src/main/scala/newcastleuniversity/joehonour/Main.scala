package newcastleuniversity.joehonour

import newcastleuniversity.joehonour.messages.MovementObserved
import newcastleuniversity.joehonour.movement_detection.Detectors
import newcastleuniversity.joehonour.output_streams.OutputStreams
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object Main {

  def main(args: Array[String]) {

    //build configuration
    val properties = CommandLineParser.parseCommandLineArguments(args)

    //build data source
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //run detections
    val sourceOfDetectedObjects = env
      .addSource(InputStreams.kafkaStreamForFrameMessageTopic(properties))
      .flatMap { _.detected_objects }

    //walking detector
    Detectors
      .walkingDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .map { walkingMovement => MovementObserved(
        walkingMovement.uuid,
        walkingMovement.movement_type,
        walkingMovement.fromLocationX,
        walkingMovement.fromLocationY,
        walkingMovement.toLocationX,
        walkingMovement.toLocationY,
        walkingMovement.averageDisplacement) }
      .addSink(OutputStreams.kafkaStreamForMovementObservedMessageTopic(properties))

    env.execute("detection-walking-task")
  }
}
