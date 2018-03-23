package newcastleuniversity.joehonour.input_streams

import java.util.Properties

import newcastleuniversity.joehonour.messages.AnomalyScore
import newcastleuniversity.joehonour.messages.deserializers.JsonAnomalyScoreDeserializer
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object InputStreams {

  def kafkaStreamForAnomalyMessageTopic(properties: Properties): FlinkKafkaConsumer011[AnomalyScore] = {
    kafkaStreamForAnomalyMessageTopic(properties.getProperty("kafka.video.anomaly.topic"), properties)
  }

  def kafkaStreamForAnomalyMessageTopic(topicName: String, properties: Properties): FlinkKafkaConsumer011[AnomalyScore] = {
    val kafkaSource = new FlinkKafkaConsumer011(
      topicName,
      new JsonAnomalyScoreDeserializer(),
      properties)
    kafkaSource.setStartFromEarliest()
    kafkaSource
  }
}
