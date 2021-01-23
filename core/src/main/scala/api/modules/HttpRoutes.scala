package api.modules

import api.Endpoints
import api.routes.{ HealthCheckRoutes, PokemonRoutes }
import cats.effect.{ ContextShift, IO, Timer }
import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.server.middleware.{ RequestLogger, ResponseLogger }
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import cats.implicits._
import org.http4s.implicits._

class HttpRoutes private (private val services: Services)(implicit C: ContextShift[IO], T: Timer[IO]) {

  private val pokemonRoute     = PokemonRoutes.make(services.translatePokemon).pokemonRoute
  private val healthCheckRoute = HealthCheckRoutes.make.healtcheckRoute

  private val swaggerRoutes = new SwaggerHttp4s(
    OpenAPIDocsInterpreter
      .toOpenAPI(
        List(Endpoints.pokemonEndpoint, Endpoints.healthcheck),
        title = "The catch them all API",
        version = "0.0.1"
      )
      .toYaml
  ).routes[IO]

  private val routes = Router(
    "/" -> healthCheckRoute,
    "/" -> (pokemonRoute <+> swaggerRoutes)
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
