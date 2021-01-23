package api.services

import api.domain.ShakespeareDescription
import api.http.clients.data.PokemonResponse.{ Pokemon, Species }
import api.http.clients.data.PokemonSpeciesResponse.{ Description, Language, SpeciesDetail }
import api.http.clients.data.TranslateShakespeareResponse.{ Contents, ShakespeareResponse }
import api.http.clients.{ PokemonApi, TranslateApi }
import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.freespec.AsyncFreeSpec

class TranslatePokemonTest extends AsyncFreeSpec with AsyncIOSpec {

  "translate happy path" in {

    val expectedDescription = "description"
    val anyPokemonName      = "anyPokemonName"

    val translateApiStub = new TranslateApi {
      override def translate(text: String): IO[ShakespeareResponse] = IO(
        ShakespeareResponse(
          Contents(
            expectedDescription
          )
        )
      )
    }

    val pokemonApiStub = new PokemonApi {
      override def getPokemon(name: String): IO[Pokemon] = IO(Pokemon(Species("speciesNname")))

      override def getPokemonSpecies(name: String): IO[SpeciesDetail] =
        IO(SpeciesDetail(List(Description("a random description", Language("en")))))
    }

    val expectedResult = Some(ShakespeareDescription(anyPokemonName, expectedDescription))
    val actual         = TranslatePokemon(pokemonApiStub, translateApiStub).translateDescriptionOf(anyPokemonName)

    actual.asserting(res => assert(res == expectedResult))

  }

  "no english description returned" in {

    val translateApiStub = new TranslateApi {
      override def translate(text: String): IO[ShakespeareResponse] =
        IO.raiseError[ShakespeareResponse](new Throwable("Should not be called!"))
    }

    val pokemonApiStub = new PokemonApi {
      override def getPokemon(name: String): IO[Pokemon] = IO(Pokemon(Species("speciesNname")))

      override def getPokemonSpecies(name: String): IO[SpeciesDetail] =
        IO(SpeciesDetail(List(Description("una descrizione causale", Language("it")))))
    }

    val expectedResult = None
    val actual         = TranslatePokemon(pokemonApiStub, translateApiStub).translateDescriptionOf("anyPokemonName")

    actual.asserting(res => assert(res == expectedResult))
  }

  "a client raise an error" in {
    val translateApiStub = new TranslateApi {
      override def translate(text: String): IO[ShakespeareResponse] =
        IO.raiseError[ShakespeareResponse](new Throwable("an error"))
    }

    val pokemonApiStub = new PokemonApi {
      override def getPokemon(name: String): IO[Pokemon] = IO.raiseError[Pokemon](new Throwable("an error"))

      override def getPokemonSpecies(name: String): IO[SpeciesDetail] =
        IO.raiseError[SpeciesDetail](new Throwable("an error"))
    }

    val actual = TranslatePokemon(pokemonApiStub, translateApiStub).translateDescriptionOf("anyPokemonName")

    actual.assertThrows[Throwable]
  }
}
