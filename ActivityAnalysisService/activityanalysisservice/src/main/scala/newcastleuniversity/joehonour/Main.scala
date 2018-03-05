package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.messages.{DetectedObject, MovementObserved}
import newcastleuniversity.joehonour.movement_detection.WalkingDetectors
import newcastleuniversity.joehonour.movement_detection.detectors.{RunningDetectors, StandingDetectors}
import newcastleuniversity.joehonour.output_streams.OutputStreams
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object Main {

  def main(args: Array[String]) {

    val properties = CommandLineParser.parseCommandLineArguments(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val sourceOfDetectedObjects = env
      .addSource(InputStreams.kafkaStreamForFrameMessageTopic(properties))
      .flatMap { _.detected_objects }

    //detectors
    registerStandingPersonDetector(sourceOfDetectedObjects, properties)
    registerWalkingPersonDetector(sourceOfDetectedObjects, properties)
    registerRunningPersonDetector(sourceOfDetectedObjects, properties)

    env.execute("object-detection-task")
  }

  def registerStandingPersonDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                     properties: Properties): DataStreamSink[MovementObserved] = {
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
  }

  def registerWalkingPersonDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                    properties: Properties): DataStreamSink[MovementObserved] = {
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
  }

  def registerRunningPersonDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                    properties: Properties): DataStreamSink[MovementObserved] = {
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
  }
}
