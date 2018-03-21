package newcastleuniversity.joehonour.output_streams

import java.util.Properties

import newcastleuniversity.joehonour.messages.MovementObserved
import newcastleuniversity.joehonour.messages.serializers.JsonMovementObservedSerializer
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011

object OutputStreams {

  def kafkaStreamForMovementObservedMessageTopic(properties: Properties): FlinkKafkaProducer011[MovementObserved] = {
    kafkaStreamForMovementObservedMessageTopic(
      properties.getProperty("bootstrap.servers"),
      properties.getProperty("kafka.activity.output.topic"))
  }

  def kafkaStreamForMovementObservedMessageTopic(brokerList: String, topicId: String): FlinkKafkaProducer011[MovementObserved] = {
    new FlinkKafkaProducer011[MovementObserved](
      brokerList,
      topicId,
      new JsonMovementObservedSerializer)
  }
}
