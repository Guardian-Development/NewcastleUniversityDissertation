package newcastleuniversity.joehonour.movement_detection.detectors

import newcastleuniversity.joehonour.messages.DetectedObject
import newcastleuniversity.joehonour.movement_detection.aggregators.MovementObjectDisplacementAggregator
import newcastleuniversity.joehonour.movement_detection.movements.DetectedMovement
import newcastleuniversity.joehonour.movement_detection.objects.{MovementObject, PositionalObject, PositionalObjectProducer}
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala.{DataStream, _}
import org.apache.flink.util.Collector

object MovementDetector {

  def builder(detectorName: () => String): MovementDetectorBuilder = {
    new MovementDetectorBuilder(detectorName)
  }

}


class MovementDetectorBuilder(val detectorName: () => String) {

  private var typeIdentifier: Option[String] = Option.empty
  private var windowSize: Long = 10
  private var windowSlide: Long = 10
  private var aggregator: Option[AggregateFunction[PositionalObject, MovementObject, MovementObject]] = Option.empty
  private var objectDisplacementRangePattern: Pattern[MovementObject, MovementObject] = _
  private var activityRepetition: Int = 1

  def objectTypeIdentifier(typeIdentifier: String) : MovementDetectorBuilder = {
    this.typeIdentifier = Option(typeIdentifier)
    this
  }

  def activityWindow(windowSize: Long, windowSlide: Long) : MovementDetectorBuilder = {
    this.windowSize = windowSize
    this.windowSlide = windowSlide
    this
  }

  def displacementAggregator(aggregator: AggregateFunction[PositionalObject, MovementObject, MovementObject]): MovementDetectorBuilder = {
    this.aggregator = Option(aggregator)
    this
  }

  def objectDisplacementIdentifyingRange(minimum: Double, maximum: Double) : MovementDetectorBuilder = {
    objectDisplacementRangePattern = Pattern.begin[MovementObject](detectorName())
      .where { person => person.displacement >= minimum }
      .where { person => person.displacement <= maximum }
    this
  }

  def activityRepetitionToTrigger(repetition: Int) : MovementDetectorBuilder = {
    this.activityRepetition = repetition
    this
  }

  def buildDetectionStream[T <: DetectedMovement](dataStream: DataStream[DetectedObject],
                                                  converter: (Iterable[MovementObject]) => T)(implicit evidence: TypeInformation[T]): DataStream[T] = {

    val positionOfObject = convertObjectToPositionalObjectStream(dataStream)
    val movementCalculationsOfObject = aggregateObjectMovementsWithinWindow(positionOfObject)
    val movementPattern = createMovementPatternDetector()

    val pattern = CEP.pattern(
      movementCalculationsOfObject.keyBy { _.uuid },
      movementPattern)

    val keyForObjects = detectorName()

    pattern.flatSelect{
      (objects: collection.Map[String, Iterable[MovementObject]], collector: Collector[T]) => {
        val objectsContributingToPattern = objects(keyForObjects)
        collector.collect(converter(objectsContributingToPattern))
      }
    }
  }

  private def convertObjectToPositionalObjectStream(dataStream: DataStream[DetectedObject]) = {
    typeIdentifier match {
      case Some(identifier) =>
        dataStream.filter { detectedObject => detectedObject.`type` == identifier }
      case None =>
    }

    dataStream
      .map { detected_object => PositionalObjectProducer
        .positionObjectFor(
          detected_object.uuid,
          detected_object.x_position,
          detected_object.y_position,
          detected_object.width,
          detected_object.height)
      }
  }

  private def aggregateObjectMovementsWithinWindow(dataStream: DataStream[PositionalObject]) = {
    dataStream
      .keyBy{ _.uuid }
      .countWindow(windowSize, windowSlide)
      .aggregate(aggregator.getOrElse(new MovementObjectDisplacementAggregator))
  }

  private def createMovementPatternDetector() = {
    objectDisplacementRangePattern
      .timesOrMore(activityRepetition)
      .consecutive()
  }
}