package newcastleuniversity.joehonour

import newcastleuniversity.joehonour.input_streams.InputStreams
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object Main {

  def main(args: Array[String]) {

    val properties = CommandLineParser.parseCommandLineArguments(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val sourceOfDetectedObjects = env
      .addSource(InputStreams.kafkaStreamForAnomalyMessageTopic(properties))

    sourceOfDetectedObjects.print()

    env.execute("object-detection-task")
  }
}
