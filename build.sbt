import Dependencies._

inThisBuild(
  List(
    organization := "azanin",
    developers := List(
      Developer("azanin", "Alessandro Zanin", "ale.zanin90@gmail.com", url("https://github.com/azanin"))
    ),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    pomIncludeRepository := { _ => false }
  )
)

val projectName = "catch-them-all"

// General Settings
lazy val commonSettings = Seq(
  scalaVersion := "2.13.3",
  scalafmtOnCompile := true,
  addCompilerPlugin(kindProjector),
  addCompilerPlugin(betterMonadicFor),
  libraryDependencies ++= Seq(
    http4sServer,
    http4sCirce,
    http4sDsl,
    http4sClient,
    catsEffect,
    circeLiteral,
    scalaTest
  )
)

lazy val core = project
  .in(file("core"))
  .settings(commonSettings)
  .settings(name := projectName)
  .settings(parallelExecution in Test := false)
  .settings(test in assembly := {})
  .settings(assemblyJarName in assembly := projectName + ".jar")
  .settings(assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") => MergeStrategy.singleOrError
    case x                                                                            =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  })
  .enablePlugins(DockerPlugin)
  .settings(dockerfile in docker := {
    dockerFile(assembly.value)
  })
  .settings(
    imageNames in docker := Seq(
      // Sets the latest tag
      ImageName(
        namespace = Some(organization.value + "/" + projectName),
        repository = projectName,
        registry = Some("ghcr.io"),
        tag = Some("latest")
      ), // Sets a name with a tag that contains the project version
      ImageName(
        namespace = Some(organization.value + "/" + projectName),
        repository = projectName,
        registry = Some("ghcr.io"),
        tag = Some("v" + version.value.replace('+', '-'))
      )
    )
  )
  .settings(
    publish in docker := Some("Github container registry" at "https://ghcr.io")
  )

lazy val tests = project
  .in(file("tests"))
  .settings(name := "tests")
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(commonSettings)
  .settings(parallelExecution in IntegrationTest := false)
  .enablePlugins(NoPublishPlugin)
  .settings(fork in IntegrationTest := true)
  .enablePlugins(DockerPlugin)
  .settings(dockerfile in docker := dockerFile((assembly in core).value))
  .dependsOn(core)

lazy val catchThemAll = project
  .in(file("."))
  .enablePlugins(NoPublishPlugin)
  .aggregate(core, tests)

def dockerFile(dependsOn: File) = {
  val artifactTargetPath = s"/app/${dependsOn.name}"

  new Dockerfile {
    from("openjdk:11-jre")
    add(dependsOn, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
    expose(80)
    label("org.containers.image.source", s"https://github.com/azanin/${projectName}")
  }
}

addCommandAlias("integrationTests", ";project tests;docker;it:test")
addCommandAlias("dockerBuildAndPublish", ";project core;dockerBuildAndPush")
