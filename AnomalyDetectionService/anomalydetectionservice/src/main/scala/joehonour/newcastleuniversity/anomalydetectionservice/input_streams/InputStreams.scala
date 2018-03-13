package joehonour.newcastleuniversity.anomalydetectionservice.input_streams

import joehonour.newcastleuniversity.anomalydetectionservice.CommandLineConfiguration
import joehonour.newcastleuniversity.anomalydetectionservice.messages.MovementObserved
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

object InputStreams {

  def kafkaStreamForMovementObservedMessageTopic(config: CommandLineConfiguration, stream: StreamingContext): DStream[MovementObserved] = {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> config.bootstrap_servers(),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "anomaly-detection-service",
      "auto.offset.reset" -> "earliest"
    )

    val topics = Array(config.activity_analysis_topic())
    val kafkaStream = KafkaUtils.createDirectStream[String, String](
      stream,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    kafkaStream
      .map { _.value }
      .map { MovementObserved.fromJson }
  }
}
