import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._


object Dependencies {
  object Versions {
    val JodaTime = "2.9.2"
    val ScalaTest = "3.0.0-RC2"
  }

  val JodaTime = Def.setting(Seq(
    "joda-time" % "joda-time" % Versions.JodaTime
  ))

  val ScalaTest = Def.setting(Seq(
    "org.scalatest" %%% "scalatest" % Versions.ScalaTest % "test"
  ))
}
