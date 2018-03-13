package joehonour.newcastleuniversity.anomalydetectionservice

import joehonour.newcastleuniversity.anomalydetectionservice.input_streams.InputStreams
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Main {

  def main(args: Array[String]): Unit = {

    val config = new CommandLineConfiguration(args)
    val sparkConf = new SparkConf().setMaster(config.master()).setAppName("anomaly-detection-service")
    val stream = new StreamingContext(sparkConf, Seconds(config.spark_interval()))

    InputStreams
      .kafkaStreamForMovementObservedMessageTopic(config, stream)
      .print()

    stream.start()
    stream.awaitTermination()
  }
}
