package joehonour.newcastleuniversity.anomalydetectionservice.output_streams

import java.util.Properties

import joehonour.newcastleuniversity.anomalydetectionservice.CommandLineConfiguration
import joehonour.newcastleuniversity.anomalydetectionservice.messages.AnomalyScore
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.streaming.dstream.DStream

object OutputStreams {

  def kafkaStreamForAnomalyScore(inputStream: DStream[AnomalyScore], config: CommandLineConfiguration): Unit = {

    val properties = kafkaProperties(config)
    val anomalyScoreTopic = config.anomaly_score_topic()

    inputStream
      .map(AnomalyScore.toJson)
      .map(t => new ProducerRecord[String, Array[Byte]](anomalyScoreTopic, t.getBytes))
      .foreachRDD(t => {
        t.foreachPartition(q => {
          val kafkaProducer = kafkaProducerFor(properties)
          q.foreach(r => {
            val meta = kafkaProducer.send(r).get()
            println(meta)
          })
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
