package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.messages.{DetectedObject, MovementObserved}
import newcastleuniversity.joehonour.movement_detection.WalkingDetectors
import newcastleuniversity.joehonour.movement_detection.detectors.{DrivingDetectors, ParkedDetectors, RunningDetectors, StandingDetectors}
import newcastleuniversity.joehonour.movement_detection.movements.DetectedMovement
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
    registerParkedCarDetector(sourceOfDetectedObjects, properties)
    registerDrivingCarDetector(sourceOfDetectedObjects, properties)

    env.execute("object-detection-task")
  }

  def registerStandingPersonDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                     properties: Properties): DataStreamSink[MovementObserved] = {
    val standingDetector = StandingDetectors
      .standingDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .map { _.asInstanceOf[DetectedMovement] }

    registerBehaviourDetectedOutput(standingDetector, properties)
  }

  def registerWalkingPersonDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                    properties: Properties): DataStreamSink[MovementObserved] = {
    val walkingDetector = WalkingDetectors
      .walkingDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .map { _.asInstanceOf[DetectedMovement] }

    registerBehaviourDetectedOutput(walkingDetector, properties)
  }

  def registerRunningPersonDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                    properties: Properties): DataStreamSink[MovementObserved] = {
    val runningDetector = RunningDetectors
      .runningDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .map { _.asInstanceOf[DetectedMovement] }

    registerBehaviourDetectedOutput(runningDetector, properties)
  }

  def registerParkedCarDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                properties: Properties): DataStreamSink[MovementObserved] = {
    val parkedDetector = ParkedDetectors
      .parkedDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .map { _.asInstanceOf[DetectedMovement] }

    registerBehaviourDetectedOutput(parkedDetector, properties)
  }

  def registerDrivingCarDetector(sourceOfDetectedObjects: DataStream[DetectedObject],
                                 properties: Properties): DataStreamSink[MovementObserved] = {
    val drivingDetector = DrivingDetectors
      .drivingDetectionStreamFrom(sourceOfDetectedObjects, properties)
      .map { _.asInstanceOf[DetectedMovement] }

    registerBehaviourDetectedOutput(drivingDetector, properties)
  }

  def registerBehaviourDetectedOutput(sourceOfBehaviourAnalysis: DataStream[DetectedMovement],
                                      properties: Properties): DataStreamSink[MovementObserved] = {
    val behaviourDetected = sourceOfBehaviourAnalysis.map { runningMovement => MovementObserved(
      runningMovement.uuid,
      runningMovement.movement_type,
      runningMovement.fromLocationX,
      runningMovement.fromLocationY,
      runningMovement.toLocationX,
      runningMovement.toLocationY,
      runningMovement.averageDisplacement)}

    behaviourDetected.print()

    behaviourDetected
      .addSink(OutputStreams.kafkaStreamForMovementObservedMessageTopic(properties))
  }
}
