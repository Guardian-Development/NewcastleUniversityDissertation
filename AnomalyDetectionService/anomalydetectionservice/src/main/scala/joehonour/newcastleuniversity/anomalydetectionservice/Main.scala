package joehonour.newcastleuniversity.anomalydetectionservice

import joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.KMeansStreamPredictorBuilder
import joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.outputs.IdentifiableVector
import joehonour.newcastleuniversity.anomalydetectionservice.input_streams.InputStreams
import joehonour.newcastleuniversity.anomalydetectionservice.messages.MovementObserved
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.StreamingKMeans
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Main {

  def main(args: Array[String]): Unit = {

    val config = new CommandLineConfiguration(args)
    val sparkConf = new SparkConf().setMaster(config.master()).setAppName("anomaly-detection-service")
    val context = new StreamingContext(sparkConf, Seconds(config.spark_interval()))

    val inputStream = InputStreams
      .kafkaStreamForMovementObservedMessageTopic(config, context)

    KMeansStreamPredictorBuilder
      .buildKMeansDistancePredictor[MovementObserved](
        inputStream,
        () => new StreamingKMeans().setK(config.activity_anomaly_k_amount()).setRandomCenters(6, 0, 1),
        MovementObserved.toVector,
        t => IdentifiableVector(t.uuid, MovementObserved.toVector(t)))
      .print()

    context.start()
    context.awaitTermination()
  }
}
