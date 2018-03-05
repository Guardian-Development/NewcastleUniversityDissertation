package newcastleuniversity.joehonour.movement_detection.detectors

import java.util.Properties

import newcastleuniversity.joehonour.messages.DetectedObject
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import newcastleuniversity.joehonour.movement_detection.movements.RunningMovement
import org.apache.flink.streaming.api.scala.{DataStream, _}

object RunningDetectors {

  def runningDetectionStreamFrom(dataStream: DataStream[DetectedObject], properties: Properties) : DataStream[RunningMovement] = {
    runningDetectionStreamFrom(
      dataStream,
      properties.getProperty("detectors.running.item.type"),
      properties.getProperty("detectors.running.window.size").toInt,
      properties.getProperty("detectors.running.window.slide").toInt,
      properties.getProperty("detectors.running.displacement.min").toDouble,
      properties.getProperty("detectors.running.displacement.max").toDouble,
      properties.getProperty("detectors.running.repetition.triggers").toInt)
  }

  def runningDetectionStreamFrom(dataStream: DataStream[DetectedObject],
                                 itemType: String,
                                 windowSize: Int,
                                 windowSlide: Int,
                                 displacementMin: Double,
                                 displacementMax: Double,
                                 repetitionTrigger: Int): DataStream[RunningMovement] = {
    MovementDetector.builder{ () => "person-running-detector" }
      .objectTypeIdentifier(itemType)
      .activityWindow(windowSize, windowSlide)
      .displacementAggregator(new MovementObjectDisplacementAggregator)
      .objectDisplacementIdentifyingRange(displacementMin, displacementMax)
      .activityRepetitionToTrigger(repetitionTrigger)
      .buildDetectionStream(dataStream, RunningMovement.buildRunningMovementFrom)
  }
}
