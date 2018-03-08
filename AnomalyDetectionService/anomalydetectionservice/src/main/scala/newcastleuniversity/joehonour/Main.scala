package newcastleuniversity.joehonour

import newcastleuniversity.joehonour.input_streams.InputStreams
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object Main {
  def main(args: Array[String]) {

    val properties = CommandLineParser.parseCommandLineArguments(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val sourceOfActivities = env
      .addSource(InputStreams.kafkaStreamForDetectedActivitiesMessageTopic(properties))

    sourceOfActivities.print()

    env.execute("anomaly-detection-task")
  }
}
