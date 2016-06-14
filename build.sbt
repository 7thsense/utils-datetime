val scala211Version = "2.11.8"
val scala210Version = "2.10.5"


lazy val ssUtilsDatetimeRoot = project.in(file("."))
  .aggregate(ssUtilsDatetimeJS, ssUtilsDatetimeJVM)
  .settings(
    publish := {},
    publishLocal := {},
    crossScalaVersions := Seq(scala211Version),
    scalaVersion := scala211Version
  )

lazy val ssUtilsDatetime = crossProject.crossType(CrossType.Full).in(file("."))
  .settings(
    organization := "com.theseventhsense",
    version := "0.1.0",
    name := "utils-datetime",
    isSnapshot := false,
    publishMavenStyle := true,
    bintrayOrganization := Some("7thsense"),
    licenses +=("MIT", url("http://opensource.org/licenses/MIT")),
    crossScalaVersions := Seq(scala211Version),
    scalaVersion := scala211Version,
    libraryDependencies ++= Dependencies.ScalaTest.value
  )
  .jvmSettings(
    libraryDependencies ++= Dependencies.JodaTime.value
  )
  .jsSettings(
    libraryDependencies += "io.github.widok" %%% "scala-js-momentjs" % "0.1.5"
  )

lazy val ssUtilsDatetimeJVM = ssUtilsDatetime.jvm

lazy val ssUtilsDatetimeJS = ssUtilsDatetime.js
