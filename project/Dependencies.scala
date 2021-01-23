import sbt._

object Dependencies {
  val catsV = "2.3.1"

  val kindProjectorV = "0.11.3"

  val betterMonadicForV = "0.3.1"

  val scalaTestV = "3.2.3"

  val http4sV = "0.21.15"

  val circeV = "0.13.0"

  val catsEffectScalaTestV = "0.4.2"

  val tapirV = "0.17.7"

  val logbackV = "1.2.3"

  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV

  val catsEffect = "org.typelevel" %% "cats-effect" % catsV

  val kindProjector = "org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full

  val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % betterMonadicForV

  val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sV

  val http4sServer = "org.http4s" %% "http4s-blaze-server" % http4sV

  val http4sClient = "org.http4s" %% "http4s-blaze-client" % http4sV

  val http4sCirce = "org.http4s" %% "http4s-circe" % http4sV

  val circeLiteral = "io.circe" %% "circe-literal" % circeV

  val cateEffectTesting = "com.codecommit" %% "cats-effect-testing-scalatest" % catsEffectScalaTestV

  val tapirCore = "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirV

  val tapirHttp4sServer = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirV

  val tapirJsonCirce = "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirV

  val circeGeneric = "io.circe" %% "circe-generic" % circeV

  val logback = "ch.qos.logback" % "logback-classic" % logbackV % Runtime

}
