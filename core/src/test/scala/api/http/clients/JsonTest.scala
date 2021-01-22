package api.http.clients

import api.http.clients.data.PokemonResponse.{ Pokemon, Species }
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite
import io.circe.literal._
import Json._
import api.http.clients.data.PokemonSpeciesResponse.{ Description, Language, SpeciesDetail }
import api.http.clients.data.TranslateShakespeareResponse.{ Contents, ShakespeareResponse }

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

  test("decode translate shakespeare text response") {

    val input =
      json"""
        {
          "contents": {
            "text": "to be translated",
            "translated": "translated",
            "translation": "shakespeare"
          },
          "success": {
            "total": 1
          }
        }"""

    val expectedResult = ShakespeareResponse(Contents("translated")).asRight

    val actual = shakespeareDecoder.decodeJson(input)

    assert(actual == expectedResult)
  }

  test("error decoding translate shakespeare text response") {

    val input =
      json"""
        {
          "contents": {
            "text": "to be translated",
            "translation": "shakespeare"
          },
          "success": {
            "total": 1
          }
        }"""

    val actual = shakespeareDecoder.decodeJson(input)

    assert(actual.isLeft)
  }
}
