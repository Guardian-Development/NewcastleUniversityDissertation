package newcastleuniversity.joehonour.movement_detection

import java.util.Properties

import newcastleuniversity.joehonour.messages.DetectedObject
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import newcastleuniversity.joehonour.movement_detection.movements.WalkingMovement
import org.apache.flink.streaming.api.scala.{DataStream, _}

package object detectors {

  def walkingDetectionStreamFrom(dataStream: DataStream[DetectedObject], properties: Properties) : DataStream[WalkingMovement] = {
    walkingDetectionStreamFrom(
      dataStream,
      properties.getProperty("detectors.walking.item.type"),
      properties.getProperty("detectors.walking.window.size").toInt,
      properties.getProperty("detectors.walking.window.slide").toInt,
      properties.getProperty("detectors.walking.displacement.min").toDouble,
      properties.getProperty("detectors.walking.displacement.max").toDouble,
      properties.getProperty("detectors.walking.repetition.triggers").toInt)
  }

  def walkingDetectionStreamFrom(dataStream: DataStream[DetectedObject],
                                 itemType: String,
                                 windowSize: Int,
                                 windowSlide: Int,
                                 displacementMin: Double,
                                 displacementMax: Double,
                                 repetitionTrigger: Int): DataStream[WalkingMovement] = {
    MovementDetector.builder{ () => "person-detector" }
      .objectTypeIdentifier(itemType)
      .activityWindow(windowSize, windowSlide)
      .displacementAggregator(new MovementObjectDisplacementAggregator)
      .objectDisplacementIdentifyingRange(displacementMin, displacementMax)
      .activityRepetitionToTrigger(repetitionTrigger)
      .buildDetectionStream(dataStream, WalkingMovement.buildWalkingMovementFrom)
  }
}
