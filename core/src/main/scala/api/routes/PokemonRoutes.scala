package api.routes

import api.Endpoints
import api.domain.ShakespeareDescription
import api.services.TranslatePokemon
import cats.effect.{ ContextShift, IO, Timer }
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.implicits._

class PokemonRoutes private (private val translatePokemon: TranslatePokemon)(implicit
  CD: ContextShift[IO],
  T: Timer[IO]
) {

  val pokemonRoute: HttpRoutes[IO] =
    Http4sServerInterpreter.toRoutes(Endpoints.pokemonEndpoint) { name =>
      translatePokemon
        .translateDescriptionOf(name)
        .map(opt => opt.fold(().asLeft[ShakespeareDescription])(desc => desc.asRight[Unit]))
    }

}

object PokemonRoutes {

  def make(translatePokemon: TranslatePokemon)(implicit CD: ContextShift[IO], T: Timer[IO]) = new PokemonRoutes(
    translatePokemon
  )
}
