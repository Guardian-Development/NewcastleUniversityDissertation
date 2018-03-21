package newcastleuniversity.joehonour

import java.util.Properties

import newcastleuniversity.joehonour.messages.Frame
import newcastleuniversity.joehonour.messages.deserializers.JsonFrameDeserializer
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
}
