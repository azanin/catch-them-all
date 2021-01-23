package api

import api.configurations.Configurations
import api.modules.{ HttpClients, HttpRoutes, Services }
import cats.effect.{ ExitCode, IO, IOApp }
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object Application extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    ApplicationResources.make.use { appResource =>
      for {
        configurations <- Configurations.load
        clients         = HttpClients(appResource, configurations)
        services        = Services.make(clients)
        routes          = HttpRoutes.make(services)
        _              <- BlazeServerBuilder[IO](ExecutionContext.global)
                            .bindHttp(Configurations.serverPort, Configurations.serverHost)
                            .withHttpApp(routes.httpApp)
                            .serve
                            .compile
                            .drain
      } yield ExitCode.Success
    }
}
