package newcastleuniversity.joehonour

import newcastleuniversity.joehonour.messages.MovementObserved
import newcastleuniversity.joehonour.movement_detection.WalkingDetectors
import newcastleuniversity.joehonour.movement_detection.detectors.{RunningDetectors, StandingDetectors}
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
    val walkingDetector = WalkingDetectors
      .walkingDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .map { walkingMovement => MovementObserved(
        walkingMovement.uuid,
        walkingMovement.movement_type,
        walkingMovement.fromLocationX,
        walkingMovement.fromLocationY,
        walkingMovement.toLocationX,
        walkingMovement.toLocationY,
        walkingMovement.averageDisplacement) }

    walkingDetector.print()

    walkingDetector
      .addSink(OutputStreams.kafkaStreamForMovementObservedMessageTopic(properties))

    //running detector
    val runningDetector = RunningDetectors
        .runningDetectionStreamFrom(sourceOfDetectedObjects, properties)
        .map { runningMovement => MovementObserved(
          runningMovement.uuid,
          runningMovement.movement_type,
          runningMovement.fromLocationX,
          runningMovement.fromLocationY,
          runningMovement.toLocationX,
          runningMovement.toLocationY,
          runningMovement.averageDisplacement) }

    runningDetector.print()

    runningDetector
      .addSink(OutputStreams.kafkaStreamForMovementObservedMessageTopic(properties))

    //standing detector
    val standingDetector = StandingDetectors
        .standingDetectionStreamFrom(sourceOfDetectedObjects, properties)
        .map { runningMovement => MovementObserved(
          runningMovement.uuid,
          runningMovement.movement_type,
          runningMovement.fromLocationX,
          runningMovement.fromLocationY,
          runningMovement.toLocationX,
          runningMovement.toLocationY,
          runningMovement.averageDisplacement)}

    standingDetector.print()

    standingDetector
      .addSink(OutputStreams.kafkaStreamForMovementObservedMessageTopic(properties))

    env.execute("detection-walking-task")
  }
}
