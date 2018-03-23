package newcastleuniversity.joehonour

import newcastleuniversity.joehonour.alerts.detectors.AlertDetector
import newcastleuniversity.joehonour.alerts.outputs.EmailNotificationOutput
import newcastleuniversity.joehonour.input_streams.InputStreams
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object Main {

  def main(args: Array[String]) {

    val properties = CommandLineParser.parseCommandLineArguments(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val alertDetector = new AlertDetector(properties)
    val emailNotificationSystem = new EmailNotificationOutput(properties)

    val anomaliesToSendAlertsFor = env
      .addSource(InputStreams.kafkaStreamForAnomalyMessageTopic(properties))
      .filter(alertDetector.anomalyRequiresAlert(_))

    anomaliesToSendAlertsFor
      .addSink(emailNotificationSystem.sendEmailForAnomaly(_))

    anomaliesToSendAlertsFor.print()

    env.execute("anomaly-alert-task")
  }
}