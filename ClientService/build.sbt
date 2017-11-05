name := BuildConfig.appName
version := BuildConfig.appVersion

lazy val scalaV = "2.11.8"

lazy val root = (project in file(".")).enablePlugins(sbtdocker.DockerPlugin, PlayScala)

name := BuildConfig.appName
version := BuildConfig.appVersion
scalaVersion := scalaV
libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)
dockerfile in docker := {
  val appDir = stage.value
  val targetDir = "/app"

  new Dockerfile {
    from("java")
    entryPoint(s"$targetDir/bin/${executableScriptName.value}")
    copy(appDir, targetDir)
  }
}
buildOptions in docker := BuildOptions(cache = false)

fork in run := true