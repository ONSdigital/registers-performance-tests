import sbt.Keys.organizationName

lazy val Versions = new {
  val scala = "2.12.6"
  val appVersion = "0.1-SNAPSHOT"
  val gatlingVersion = "2.3.1"
}

lazy val Constant = new {
  val appName = "performance-tests"
  val projectStage = "alpha"
  val organizationName = "ons"
  val organization = "uk.gov.ons"
  val team = "registers"
}

lazy val commonSettings = Seq(
  scalaVersion := Versions.scala,
  scalacOptions in ThisBuild ++= Seq(
    "-language:experimental.macros",
    "-target:jvm-1.8",
    "-encoding", "UTF-8",
    "-language:reflectiveCalls",
    "-language:experimental.macros",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:postfixOps",
    "-deprecation", // warning and location for usages of deprecated APIs
    "-feature", // warning and location for usages of features that should be imported explicitly
    "-unchecked", // additional warnings where generated code depends on assumptions
    "-Xlint", // recommended additional warnings
    "-Xcheckinit", // runtime error when a val is not initialized due to trait hierarchies (instead of NPE somewhere else)
    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures
    "-Ywarn-dead-code", // Warn when dead code is identified
    "-Ywarn-unused", // Warn when local and private vals, vars, defs, and types are unused
    "-Ywarn-numeric-widen" // Warn when numerics are widened
  )
)

scalaSource in Gatling := baseDirectory.value / "src/test/scala"


lazy val root = (project in file("."))
  .enablePlugins(GatlingPlugin)
  .settings(inConfig(Gatling)(Defaults.testSettings): _*)
  .settings(commonSettings: _*)
  .settings(
    organizationName := s"${Constant.organizationName}",
    organization := s"${Constant.organization}",
    moduleName := s"${Constant.appName}",
    version := s"${Versions.appVersion}",
    name := s"${organizationName.value}-${Constant.team}-${moduleName.value}",
    version := (version in ThisBuild).value,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "com.typesafe" % "config" % "1.3.2",
      "io.gatling.highcharts" % "gatling-charts-highcharts" % Versions.gatlingVersion % "test",
      "io.gatling" % "gatling-test-framework" % Versions.gatlingVersion % "test",
      "io.github.lukehutch" % "fast-classpath-scanner" % "3.1.9" % "test"
    )
  )