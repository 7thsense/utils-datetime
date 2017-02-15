val scala211Version = "2.11.8"

resolvers in ThisBuild ++= Seq(
  Resolver.bintrayRepo("easel", "maven"),
  Resolver.bintrayRepo("7thsense", "maven")
)

lazy val ssUtilsDatetimeRoot = project
  .in(file("."))
  .aggregate(ssUtilsDatetimeJS,
             ssUtilsDatetimeJVM,
             ssUtilsDatetimeCodecsCirceJS,
             ssUtilsDatetimeCodecsCirceJVM,
             ssUtilsDatetimeCodecsPlay)
  .settings(
    publish := {},
    publishLocal := {},
    crossScalaVersions := Seq(scala211Version),
    scalaVersion := scala211Version
  )

val CommonSettings = Seq(
  organization := "com.theseventhsense",
  version := "0.1.12",
  isSnapshot := false,
  publishMavenStyle := true,
  bintrayOrganization := Some("7thsense"),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  crossScalaVersions := Seq(scala211Version),
  scalaVersion := scala211Version
)

lazy val ssUtilsDatetime = crossProject
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(CommonSettings: _*)
  .settings(
    name := "utils-datetime",
    libraryDependencies ++= Dependencies.Cats.value ++ Dependencies.ScalaTest.value
  )
  .jsSettings(jsEnv := NodeJSEnv().value)
  .jsSettings(
    libraryDependencies += "io.github.widok" %%% "scala-js-momentjs" % "0.1.6.ss.1"
  )

lazy val ssUtilsDatetimeJVM = ssUtilsDatetime.jvm

lazy val ssUtilsDatetimeJS = ssUtilsDatetime.js

lazy val ssUtilsDatetimeCodecsCirce = crossProject
  .crossType(CrossType.Pure)
  .in(file("./codecs-circe"))
  .dependsOn(ssUtilsDatetime)
  .settings(CommonSettings: _*)
  .settings(
    name := "utils-datetime-circe",
    libraryDependencies ++= Dependencies.Circe.value
  )

lazy val ssUtilsDatetimeCodecsCirceJVM = ssUtilsDatetimeCodecsCirce.jvm

lazy val ssUtilsDatetimeCodecsCirceJS = ssUtilsDatetimeCodecsCirce.js

lazy val ssUtilsDatetimeCodecsPlay = project
  .in(file("./codecs-playjson"))
  .dependsOn(ssUtilsDatetime.jvm)
  .settings(CommonSettings: _*)
  .settings(
    name := "utils-datetime-playjson",
    libraryDependencies ++= Dependencies.PlayJson.value
  )
