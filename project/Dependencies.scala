import sbt._

object Dependencies {
  val catsV = "2.5.4"

  val kindProjectorV = "0.13.2"

  val betterMonadicForV = "0.3.1"

  val scalaTestV = "3.2.11"

  val http4sV = "0.21.31"

  val circeV = "0.14.1"

  val catsEffectScalaTestV = "0.5.4"

  val tapirV = "0.17.20"

  val logbackV = "1.2.10"

  val testContainerV = "0.40.1"

  val mockserverClientV = "5.12.0"

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

  val testContainers = "com.dimafeng" %% "testcontainers-scala-scalatest" % testContainerV

  val mockServerClient = "org.mock-server" % "mockserver-client-java" % mockserverClientV

  val mockServer = "com.dimafeng" %% "testcontainers-scala-mockserver" % testContainerV

  val tepirOpenApi = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirV

  val tapirOpenApiCirce = "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirV

  val tapirSwagger = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % tapirV

}
