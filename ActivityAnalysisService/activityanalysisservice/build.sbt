resolvers in ThisBuild ++= Seq(
    "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
    Resolver.mavenLocal
)

name := "ActivityAnalysisService"

version := "0.1-SNAPSHOT"

organization := "newcastleuniversity.joehonour"

scalaVersion in ThisBuild := "2.11.7"

val flinkVersion = "1.4.1"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided")

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )

mainClass in assembly := Some("newcastleuniversity.joehonour.Job")

// make run command include the provided dependencies
run in Compile := Defaults.runTask(fullClasspath in Compile,
                                   mainClass in (Compile, run),
                                   runner in (Compile,run)
                                  ).evaluated

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)