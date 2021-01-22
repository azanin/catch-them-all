package api.http.clients

import api.http.clients.data.PokemonResponse.{ Pokemon, Species }
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite
import io.circe.literal._
import Json._
import api.http.clients.data.PokemonSpeciesResponse.{ Description, Language, SpeciesDetail }

class JsonTest extends AnyFunSuite {

  test("error decoding pokemon") {

    val input =
      json"""{
             "species": {
                "url": "https://pokeapi.co/api/v2/pokemon-species/132/"
              }
            }"""

    val actual = pokemonDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("decode species antries") {

    val input =
      json"""{
             "species": {
                "name": "ditto",
                "url": "https://pokeapi.co/api/v2/pokemon-species/132/"
              }
            }"""

    val expectedResult = Pokemon(Species("ditto")).asRight
    val actual         = pokemonDecoder.decodeJson(input)

    assert(actual == expectedResult)
  }

  test("decode species details") {

    val input =
      json"""{
              "flavor_text_entries": [
                {
                    "flavor_text": "description1",
                    "language": {
                        "name": "en",
                        "url": "url"
                    }
                },
                {
                    "flavor_text": "description2",
                    "language": {
                        "name": "ja",
                        "url": "url"
                    }
                },
                {
                    "flavor_text": "description3",
                    "language": {
                        "name": "en",
                        "url": "url"
                    }
                }
              ]
            }"""

    val actual = speciesDetailDecoder.decodeJson(input)

    val expectedResult = SpeciesDetail(
      List(
        Description(
          "description1",
          Language("en")
        ),
        Description(
          "description2",
          Language("ja")
        ),
        Description(
          "description3",
          Language("en")
        )
      )
    ).asRight

    assert(actual == expectedResult)
  }

  test("error decoding species details") {

    val input =
      json"""{
              "flavor_text_entries": [
                {
                    "language": {
                        "name": "ja",
                        "url": "url"
                    }
                }
              ]
            }"""

    val actual = speciesDetailDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

}
