name := BuildConfig.appName
version := BuildConfig.appVersion

lazy val scalaV = "2.11.8"

lazy val server = (project in file("server")).settings(
  name := BuildConfig.appName,
  version := BuildConfig.appVersion,
  scalaVersion := scalaV,
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
  libraryDependencies ++= Seq(
    jdbc,
    cache,
    ws,
    "com.vmunier" %% "scalajs-scripts" % "1.0.0",
    "org.webjars" %% "webjars-play" % "2.5.0",
    "org.webjars" % "jquery" % "3.1.1-1",
    "org.webjars" % "bootstrap" % "3.3.7",
    "org.webjars" % "font-awesome" % "4.7.0",
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
  ),
  dockerfile in docker := {
      val appDir = stage.value
      val targetDir = "/app"

      new Dockerfile {
        from("java")
        entryPoint(s"$targetDir/bin/${executableScriptName.value}")
        copy(appDir, targetDir)
      }
    },
    buildOptions in docker := BuildOptions(cache = false)
).enablePlugins(sbtdocker.DockerPlugin, PlayScala).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
    "com.lihaoyi" %%% "scalatags" % "0.5.2"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

fork in run := true