package api.routes

import api.Endpoints
import api.domain.ShakespeareDescription
import api.domain.errors.Errors.ErrorInfo
import api.services.TranslatePokemon
import cats.effect.{ ContextShift, IO, Timer }
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.implicits._
import api.domain.errors.Errors._

class PokemonRoutes private (private val translatePokemon: TranslatePokemon)(implicit
  CD: ContextShift[IO],
  T: Timer[IO]
) {

  val pokemonRoute: HttpRoutes[IO] =
    Http4sServerInterpreter.toRoutes(Endpoints.pokemonEndpoint) { name =>
      translatePokemon
        .translateDescriptionOf(name)
        .map {
          case Some(desc) => desc.asRight[ErrorInfo]
          case None       => NotFound("no description available").asLeft[ShakespeareDescription].leftWiden[ErrorInfo]
        }
        .handleErrorWith {
          case x: ErrorInfo => IO(x.asLeft)
          case t            => IO(InternalServerError(t.getMessage).asLeft)
        }
    }
}

object PokemonRoutes {

  def make(translatePokemon: TranslatePokemon)(implicit CD: ContextShift[IO], T: Timer[IO]) = new PokemonRoutes(
    translatePokemon
  )
}
