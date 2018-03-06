package newcastleuniversity.joehonour.movement_detection.detectors

import java.util.Properties

import newcastleuniversity.joehonour.messages.DetectedObject
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import newcastleuniversity.joehonour.movement_detection.movements.DrivingMovement
import org.apache.flink.streaming.api.scala.{DataStream, _}

object DrivingDetectors {

  def drivingDetectionStreamFrom(dataStream: DataStream[DetectedObject], properties: Properties) : DataStream[DrivingMovement] = {
    drivingDetectionStreamFrom(
      dataStream,
      properties.getProperty("detectors.driving.item.type"),
      properties.getProperty("detectors.driving.window.size").toInt,
      properties.getProperty("detectors.driving.window.slide").toInt,
      properties.getProperty("detectors.driving.displacement.min").toDouble,
      properties.getProperty("detectors.driving.displacement.max").toDouble,
      properties.getProperty("detectors.driving.repetition.triggers").toInt)
  }

  def drivingDetectionStreamFrom(dataStream: DataStream[DetectedObject],
                                 itemType: String,
                                 windowSize: Int,
                                 windowSlide: Int,
                                 displacementMin: Double,
                                 displacementMax: Double,
                                 repetitionTrigger: Int): DataStream[DrivingMovement] = {
    MovementDetector.builder{ () => "car-driving-detector" }
      .objectTypeIdentifier(itemType)
      .activityWindow(windowSize, windowSlide)
      .displacementAggregator(new MovementObjectDisplacementAggregator)
      .objectDisplacementIdentifyingRange(displacementMin, displacementMax)
      .activityRepetitionToTrigger(repetitionTrigger)
      .buildDetectionStream(dataStream, DrivingMovement.buildDrivingMovementFrom)
  }
}
