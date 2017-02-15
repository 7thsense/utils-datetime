import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._


object Dependencies {
  object Versions {
    val Cats = "0.9.0"
    val Circe = "0.7.0"
    val PlayJson = "2.5.12"
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

  val ScalaTest = Def.setting(Seq(
    "org.scalatest" %%% "scalatest" % Versions.ScalaTest % "test"
  ))
}
