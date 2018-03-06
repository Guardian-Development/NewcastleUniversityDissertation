package newcastleuniversity.joehonour.movement_detection.detectors

import java.util.Properties

import newcastleuniversity.joehonour.messages.DetectedObject
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import newcastleuniversity.joehonour.movement_detection.movements.ParkedMovement
import org.apache.flink.streaming.api.scala.{DataStream, _}

object ParkedDetectors {

  def parkedDetectionStreamFrom(dataStream: DataStream[DetectedObject], properties: Properties) : DataStream[ParkedMovement] = {
    parkedDetectionStreamFrom(
      dataStream,
      properties.getProperty("detectors.parked.item.type"),
      properties.getProperty("detectors.parked.window.size").toInt,
      properties.getProperty("detectors.parked.window.slide").toInt,
      properties.getProperty("detectors.parked.displacement.min").toDouble,
      properties.getProperty("detectors.parked.displacement.max").toDouble,
      properties.getProperty("detectors.parked.repetition.triggers").toInt)
  }

  def parkedDetectionStreamFrom(dataStream: DataStream[DetectedObject],
                                 itemType: String,
                                 windowSize: Int,
                                 windowSlide: Int,
                                 displacementMin: Double,
                                 displacementMax: Double,
                                 repetitionTrigger: Int): DataStream[ParkedMovement] = {
    MovementDetector.builder{ () => "car-parked-detector" }
      .objectTypeIdentifier(itemType)
      .activityWindow(windowSize, windowSlide)
      .displacementAggregator(new MovementObjectDisplacementAggregator)
      .objectDisplacementIdentifyingRange(displacementMin, displacementMax)
      .activityRepetitionToTrigger(repetitionTrigger)
      .buildDetectionStream(dataStream, ParkedMovement.buildParkedMovementFrom)
  }
}

