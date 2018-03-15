package joehonour.newcastleuniversity.anomalydetectionservice

import java.util.Properties

import joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.KMeansStreamPredictorBuilder
import joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.outputs.IdentifiableVector
import joehonour.newcastleuniversity.anomalydetectionservice.input_streams.InputStreams
import joehonour.newcastleuniversity.anomalydetectionservice.messages.{AnomalyScore, MovementObserved}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.StreamingKMeans
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Main {

  def main(args: Array[String]): Unit = {

    val config = new CommandLineConfiguration(args)
    val sparkConf = new SparkConf().setMaster(config.master()).setAppName("anomaly-detection-service")
    val context = new StreamingContext(sparkConf, Seconds(config.spark_interval()))

    val anomalyDetectionStream = KMeansStreamPredictorBuilder
      .buildKMeansDistancePredictor[MovementObserved](
        InputStreams.kafkaStreamForMovementObservedMessageTopic(config, context),
        () => new StreamingKMeans().setK(config.activity_anomaly_k_amount()).setRandomCenters(6, 0, 1),
        MovementObserved.toVector,
        t => IdentifiableVector(t.uuid, MovementObserved.toVector(t)))
      .map(t => AnomalyScore(t.uuid, t.distance))

    OutputStreams
      .kafkaStreamForAnomalyScore(anomalyDetectionStream, config)

    context.start()
    context.awaitTermination()
  }
}

object OutputStreams {

  def kafkaStreamForAnomalyScore(inputStream: DStream[AnomalyScore], config: CommandLineConfiguration): Unit = {
    val props: Properties = kafkaProperties(config)

    inputStream
      .map(AnomalyScore.toJson)
      .map(t => new ProducerRecord[String, Array[Byte]]("anomaly-score-output", t.getBytes))
      .foreachRDD(t => {
        val kafkaProducer = kafkaProducerFor(props)
        t.foreachPartition(q => {
          val meta = q.map(kafkaProducer.send)
          meta.foreach { _.get() }
        })
      })
  }

  private def kafkaProducerFor(properties: Properties): KafkaProducer[String, Array[Byte]] = {
    val kafkaProducer = new KafkaProducer[String, Array[Byte]](properties)
    sys.addShutdownHook {
      kafkaProducer.close()
    }
    kafkaProducer
  }

  private def kafkaProperties(config: CommandLineConfiguration): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", config.bootstrap_servers())
    props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
    props
  }
}
