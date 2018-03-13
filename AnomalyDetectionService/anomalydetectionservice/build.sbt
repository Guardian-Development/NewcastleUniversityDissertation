lazy val root = (project in file(".")).

  settings(
    inThisBuild(List(
      organization := "joehonour.newcastleuniversity",
      scalaVersion := "2.11.8"
    )),
    name := "AnomalyDetectionService",
    version := "0.1-SNAPSHOT",
    sparkVersion := "2.3.0",
    sparkComponents := Seq("streaming", "streaming-kafka-0-10"),

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    parallelExecution in Test := false,
    fork := true,

    libraryDependencies ++= Seq(
        ("org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion.value).exclude("org.spark-project.spark", "unused")),

    // uses compile classpath for the run task, including "provided" jar (cf http://stackoverflow.com/a/21803413/3827)
    run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated
  )
