package joehonour.newcastleuniversity.anomalydetectionservice

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Main {

  def main(args: Array[String]): Unit = {

    val config = new CommandLineConfiguration(args)
    val sparkConf = new SparkConf().setMaster(config.master()).setAppName("anomaly-detection-service")
    val stream = new StreamingContext(sparkConf, Seconds(config.spark_interval()))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> config.bootstrap_servers(),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "anomaly-detection-service",
      "auto.offset.reset" -> "latest"
    )

    val topics = Array(config.activity_analysis_topic())
    val kafkaStream = KafkaUtils.createDirectStream[String, String](
      stream,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    kafkaStream.map(record => (record.key, record.value)).map(t => t._2).print()

    stream.start()
    stream.awaitTermination()
  }
}
