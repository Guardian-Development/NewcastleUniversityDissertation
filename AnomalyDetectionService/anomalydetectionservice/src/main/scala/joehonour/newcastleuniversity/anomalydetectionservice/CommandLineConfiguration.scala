package joehonour.newcastleuniversity.anomalydetectionservice

import org.rogach.scallop.{ScallopConf, ScallopOption}

class CommandLineConfiguration (arguments: Seq[String]) extends ScallopConf(arguments) {
  val master: ScallopOption[String] = opt[String](required = true)
  val spark_interval: ScallopOption[Int] = opt[Int](required = true)
  val bootstrap_servers: ScallopOption[String] = opt[String](required = true)
  val activity_analysis_topic: ScallopOption[String] = opt[String](required = true)
  verify()
}
