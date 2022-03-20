package api

import cats.effect.{ Blocker, ContextShift, IO, Resource }
import com.dimafeng.testcontainers.{ GenericContainer, MockServerContainer }
import org.http4s.client.JavaNetClientBuilder
import org.mockserver.client.MockServerClient
import org.slf4j.LoggerFactory
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait

object Resources {

  val network      = Network.newNetwork
  val networkAlias = "mockserver"
  val exposedPort  = 1080

  lazy val mockServer = MockServerContainer("5.13.0").configure { c =>
    c.withNetwork(network)
    c.withNetworkAliases(networkAlias)
    c.withExposedPorts(exposedPort)
    c.waitingFor(Wait.forLogMessage(".*started on port:.*", 1))
    c.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("MockServer")))
    ()
  }

  lazy val apiContainer = GenericContainer(
    dockerImage = "azanin/tests",
    exposedPorts = Seq(80),
    env = Map(
      "POKEMON_HOST"   -> s"http://${networkAlias}:${exposedPort}",
      "TRANSLATE_HOST" -> s"http://${networkAlias}:${exposedPort}"
    ),
    waitStrategy = Wait.forLogMessage(".*started at http://0.0.0.0:80/.*", 1)
  ).configure { provider =>
    provider.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("ApiServer")))
    provider.withNetwork(network)
    ()
  }

  private lazy val mockServerClient =
    Resource.make(IO(new MockServerClient(mockServer.container.getHost, mockServer.container.getServerPort))) {
      client =>
        IO {
          client.reset() //I have to do this because the client shutdown the server when the resource is released
          ()
        }
    }

  private def httpClient(blocker: Blocker)(implicit cs: ContextShift[IO]) = JavaNetClientBuilder[IO](blocker).resource

  def clients(blocker: Blocker)(implicit cs: ContextShift[IO]) =
    for {
      httpClient       <- httpClient(blocker)
      mockServerClient <- mockServerClient
    } yield (httpClient, mockServerClient)

}
