package newcastleuniversity.joehonour

import java.io.FileInputStream
import java.util.Properties
import org.apache.flink.api.java.utils.ParameterTool

object CommandLineParser {

  def parseCommandLineArguments(args: Array[String]): Properties ={
    val propertiesFile = ParameterTool.fromArgs(args).getRequired("properties-file")

    val properties = new Properties()
    properties.load(new FileInputStream(propertiesFile))
    properties
  }
}
