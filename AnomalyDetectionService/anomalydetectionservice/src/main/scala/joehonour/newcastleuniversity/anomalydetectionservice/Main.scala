package joehonour.newcastleuniversity.anomalydetectionservice

import joehonour.newcastleuniversity.anomalydetectionservice.input_streams.InputStreams
import joehonour.newcastleuniversity.anomalydetectionservice.messages.MovementObserved
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.StreamingKMeans
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

case class IdentifiableVector(uuid: String, vector: linalg.Vector)
case class IdentifiableDistance(uuid: String, distance: Double)

object KMeansStreamPredictorBuilder {

  def buildKMeansPredictor[T](inputStream: DStream[T],
                              kMeansProducer: () => StreamingKMeans,
                              convertToVector: (T) => linalg.Vector,
                              convertToIdentifiableVector: (T) => IdentifiableVector) = {

    val inputsToVectors = inputStream
      .map(convertToVector)

    val kMeansModel = kMeansProducer()
    kMeansModel.trainOn(inputsToVectors)

    inputStream
      .map { convertToIdentifiableVector }
      .map { m => (m.uuid, m.vector, kMeansModel.latestModel().predict(m.vector)) }
      .map { p => IdentifiableDistance(p._1, Vectors.sqdist(p._2, kMeansModel.latestModel().clusterCenters(p._3))) }
  }
}

object Main {

  def main(args: Array[String]): Unit = {

    val config = new CommandLineConfiguration(args)
    val sparkConf = new SparkConf().setMaster(config.master()).setAppName("anomaly-detection-service")
    val context = new StreamingContext(sparkConf, Seconds(config.spark_interval()))

    val inputStream = InputStreams
      .kafkaStreamForMovementObservedMessageTopic(config, context)

    KMeansStreamPredictorBuilder
      .buildKMeansPredictor[MovementObserved](
        inputStream,
        () => new StreamingKMeans().setK(5).setRandomCenters(6, 0, 1),
        toVector,
        t => IdentifiableVector(t.uuid, toVector(t)))
      .print()

    context.start()
    context.awaitTermination()
  }

  private def toVector(m: MovementObserved): linalg.Vector = {
    Vectors.dense(
      movementTypeAsInt(m.movement_type),
      m.from_position_x,
      m.from_position_y,
      m.to_position_x,
      m.to_position_y,
      m.average_displacement)
  }

  val movementTypeToInt = mutable.Map[String, Int]()
  var currentTopIndex = 0

  def movementTypeAsInt(movementType: String) : Int = {
    movementTypeToInt.get(movementType) match {
      case Some(index) => index
      case None =>
        currentTopIndex = currentTopIndex + 1
        movementTypeToInt += (movementType -> currentTopIndex)
        currentTopIndex
    }
  }
}
