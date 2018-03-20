package joehonour.newcastleuniversity.anomalydetectionservice

import joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.KMeansStreamPredictorBuilder
import joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.outputs.IdentifiableVector
import joehonour.newcastleuniversity.anomalydetectionservice.input_streams.InputStreams
import joehonour.newcastleuniversity.anomalydetectionservice.messages.{AnomalyScore, MovementObserved}
import joehonour.newcastleuniversity.anomalydetectionservice.output_streams.OutputStreams
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.StreamingKMeans
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Main {

  def main(args: Array[String]): Unit = {

    val config = new CommandLineConfiguration(args)
    val sparkConf = new SparkConf().setMaster(config.master()).setAppName("anomaly-detection-service")
    val context = new StreamingContext(sparkConf, Seconds(config.spark_interval()))

    val anomalyDetectionStream = anomalyDetectionStreamFor(config, context)

    OutputStreams
      .kafkaStreamForAnomalyScore(anomalyDetectionStream, config)

    context.start()
    context.awaitTermination()
  }

  private def anomalyDetectionStreamFor(config: CommandLineConfiguration, context: StreamingContext): DStream[AnomalyScore] = {
    KMeansStreamPredictorBuilder
      .buildKMeansDistancePredictor[MovementObserved](
      InputStreams.kafkaStreamForMovementObservedMessageTopic(config, context),
      () => new StreamingKMeans().setK(config.activity_anomaly_k_amount()).setDecayFactor(1.0).setRandomCenters(6, 0, 1),
      MovementObserved.toVector,
      t => IdentifiableVector(t.uuid, MovementObserved.toVector(t)))
      .map(t => AnomalyScore(t.uuid, t.distance))
  }
}
