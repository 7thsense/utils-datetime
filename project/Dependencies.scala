import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._


object Dependencies {
  object Versions {
    val Cats = "0.7.2"
    val Circe = "0.5.1"
    val JodaTime = "2.9.2"
    val PlayJson = "2.5.3"
    val ScalaTest = "3.0.0"
  }

  val Cats = Def.setting(Seq(
    "org.typelevel" %%% "cats" % Versions.Cats
  ))

  val Circe = Def.setting(Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic"
//    "io.circe" %% "circe-parser"
  ).map(_ % Versions.Circe))

  val PlayJson = Def.setting(Seq(
    "com.typesafe.play" %% "play-json" % Versions.PlayJson
  ))

  val JodaTime = Def.setting(Seq(
    "joda-time" % "joda-time" % Versions.JodaTime
  ))

  val ScalaTest = Def.setting(Seq(
    "org.scalatest" %%% "scalatest" % Versions.ScalaTest % "test"
  ))
}
