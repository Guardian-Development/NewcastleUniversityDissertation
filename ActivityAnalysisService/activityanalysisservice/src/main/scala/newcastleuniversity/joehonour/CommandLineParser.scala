package newcastleuniversity.joehonour

import org.apache.flink.api.java.utils.ParameterTool

case class Configuration(kafkaBootStrapServers: String,
                         kafkaTopic: String)

object CommandLineParser {

  def parseCommandLineArguments(args: Array[String]): Configuration ={
    val propertiesFile = ParameterTool.fromArgs(args).getRequired("properties-file")
    val properties = ParameterTool.fromPropertiesFile(propertiesFile)

    val bootstrapServers = properties.getRequired("kafka.bootstrap.servers")
    val kafkaTopic = properties.getRequired("kafka.topic")

    Configuration(bootstrapServers, kafkaTopic)
  }
}
