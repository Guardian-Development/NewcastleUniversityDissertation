package newcastleuniversity.joehonour.input_streams

import java.util.Properties

import newcastleuniversity.joehonour.messages.{ActivityObserved, AnomalyScore, Frame}
import newcastleuniversity.joehonour.messages.deserializers.{JsonAnomalyScoreDeserializer, JsonFrameDeserializer, JsonActivityObservedDeserializer}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object InputStreams {

  def kafkaStreamForFrameMessageTopic(properties: Properties): FlinkKafkaConsumer011[Frame] = {
    kafkaStreamForFrameMessageTopic(properties.getProperty("kafka.video.frame.topic"), properties)
  }

  def kafkaStreamForFrameMessageTopic(topicName: String, properties: Properties): FlinkKafkaConsumer011[Frame] = {
    val kafkaSource = new FlinkKafkaConsumer011(
      topicName,
      new JsonFrameDeserializer(),
      properties)
    kafkaSource.setStartFromEarliest()
    kafkaSource
  }

  def kafkaStreamForActivityMessageTopic(properties: Properties): FlinkKafkaConsumer011[ActivityObserved] = {
    kafkaStreamForActivityMessageTopic(properties.getProperty("kafka.video.activity.topic"), properties)
  }

  def kafkaStreamForActivityMessageTopic(topicName: String, properties: Properties): FlinkKafkaConsumer011[ActivityObserved] = {
    val kafkaSource = new FlinkKafkaConsumer011(
      topicName,
      new JsonActivityObservedDeserializer(),
      properties)
    kafkaSource.setStartFromEarliest()
    kafkaSource
  }

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
