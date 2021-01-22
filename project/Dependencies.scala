import sbt._

object Dependencies {
  val catsV = "2.3.1"

  val kindProjectorV = "0.11.3"

  val betterMonadicForV = "0.3.1"

  val scalaTestV = "3.2.3"

  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV

  val catsEffect = "org.typelevel" %% "cats-effect" % catsV

  val kindProjector = "org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full

  val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % betterMonadicForV
}
