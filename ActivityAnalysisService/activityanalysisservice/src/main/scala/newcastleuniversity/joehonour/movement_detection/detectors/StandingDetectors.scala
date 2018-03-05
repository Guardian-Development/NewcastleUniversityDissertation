package newcastleuniversity.joehonour.movement_detection.detectors

import java.util.Properties

import newcastleuniversity.joehonour.messages.DetectedObject
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import newcastleuniversity.joehonour.movement_detection.movements.StandingMovement
import org.apache.flink.streaming.api.scala.{DataStream, _}

object StandingDetectors {

  def standingDetectionStreamFrom(dataStream: DataStream[DetectedObject], properties: Properties) : DataStream[StandingMovement] = {
    standingDetectionStreamFrom(
      dataStream,
      properties.getProperty("detectors.standing.item.type"),
      properties.getProperty("detectors.standing.window.size").toInt,
      properties.getProperty("detectors.standing.window.slide").toInt,
      properties.getProperty("detectors.standing.displacement.min").toDouble,
      properties.getProperty("detectors.standing.displacement.max").toDouble,
      properties.getProperty("detectors.standing.repetition.triggers").toInt)
  }

  def standingDetectionStreamFrom(dataStream: DataStream[DetectedObject],
                                 itemType: String,
                                 windowSize: Int,
                                 windowSlide: Int,
                                 displacementMin: Double,
                                 displacementMax: Double,
                                 repetitionTrigger: Int): DataStream[StandingMovement] = {
    MovementDetector.builder{ () => "person-standing-detector" }
      .objectTypeIdentifier(itemType)
      .activityWindow(windowSize, windowSlide)
      .displacementAggregator(new MovementObjectDisplacementAggregator)
      .objectDisplacementIdentifyingRange(displacementMin, displacementMax)
      .activityRepetitionToTrigger(repetitionTrigger)
      .buildDetectionStream(dataStream, StandingMovement.buildStandingMovementFrom)
  }
}
