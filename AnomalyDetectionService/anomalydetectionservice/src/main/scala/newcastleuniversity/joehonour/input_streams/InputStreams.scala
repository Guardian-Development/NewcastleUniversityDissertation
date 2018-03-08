package newcastleuniversity.joehonour.input_streams

import java.util.Properties

import newcastleuniversity.joehonour.messages.MovementObserved
import newcastleuniversity.joehonour.messages.deserializers.JsonMovementObservedDeserializer
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object InputStreams {

  def kafkaStreamForDetectedActivitiesMessageTopic(properties: Properties): FlinkKafkaConsumer011[MovementObserved] = {
    kafkaStreamForDetectedActivitiesMessageTopic(properties.getProperty("kafka.video.activity.topic"), properties)
  }

  def kafkaStreamForDetectedActivitiesMessageTopic(topicName: String, properties: Properties)
    : FlinkKafkaConsumer011[MovementObserved] = {
    val kafkaSource = new FlinkKafkaConsumer011(
      topicName,
      new JsonMovementObservedDeserializer(),
      properties)
    kafkaSource.setStartFromEarliest()
    kafkaSource
  }
}
