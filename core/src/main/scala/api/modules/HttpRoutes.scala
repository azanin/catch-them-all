package api.modules

import api.routes.{ HealthCheckRoutes, PokemonRoutes }
import cats.effect.{ ContextShift, IO, Timer }
import org.http4s.HttpApp
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.middleware.{ RequestLogger, ResponseLogger }

class HttpRoutes private (private val services: Services)(implicit C: ContextShift[IO], T: Timer[IO]) {

  private val pokemonRoute     = PokemonRoutes.make(services.translatePokemon).pokemonRoute
  private val healthCheckRoute = HealthCheckRoutes.make.healtcheckRoute

  private val routes = Router(
    "/" -> healthCheckRoute,
    "/" -> pokemonRoute
  ).orNotFound

  private val loggers: HttpApp[IO] => HttpApp[IO] = {
    { http: HttpApp[IO] =>
      RequestLogger.httpApp(true, true)(http)
    } andThen { http: HttpApp[IO] =>
      ResponseLogger.httpApp(true, true)(http)
    }
  }

  val httpApp: HttpApp[IO] = loggers(routes)
}

object HttpRoutes {
  def make(services: Services)(implicit C: ContextShift[IO], T: Timer[IO]) = new HttpRoutes(services)
}
