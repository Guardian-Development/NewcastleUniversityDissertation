val sparkVersion =  "2.3.0"

lazy val root = (project in file(".")).

  settings(
    inThisBuild(List(
      organization := "joehonour.newcastleuniversity",
      scalaVersion := "2.11.8"
    )),
    name := "AnomalyDetectionService",
    version := "0.1-SNAPSHOT",
    fork := true,

    libraryDependencies ++= Seq(
        "org.rogach" %% "scallop" % "3.1.2",
        "org.apache.spark" %% "spark-streaming" % sparkVersion,
        "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
        "org.apache.spark" %% "spark-mllib" % sparkVersion),

    assemblyMergeStrategy in assembly := {
      case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
      case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
      case "log4j.properties"                                  => MergeStrategy.discard
      case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
      case "reference.conf"                                    => MergeStrategy.concat
      case _ => MergeStrategy.first
    },

    // uses compile classpath for the run task, including "provided" jar (cf http://stackoverflow.com/a/21803413/3827)
    run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated
  )
