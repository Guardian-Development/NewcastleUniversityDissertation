name := "AnomalyDetectionService"
version := "0.1-SNAPSHOT"
organization := "newcastleuniversity.joehonour"
scalaVersion in ThisBuild := "2.11.7"

val sparkVersion = "2.3.0"

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= sparkDependencies
  )

mainClass in assembly := Some("newcastleuniversity.joehonour.anomalydetectionservice.Main")

// make run command include the provided dependencies
run in Compile := Defaults.runTask(fullClasspath in Compile,
  mainClass in (Compile, run),
  runner in (Compile,run)
).evaluated

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)