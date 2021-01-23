package api.http.clients.data

import api.http.clients.data.PokemonSpeciesResponse.{ Description, Language, SpeciesDetail }
import org.scalatest.funsuite.AnyFunSuite

class PokemonSpeciesResponseTest extends AnyFunSuite {

  test("find an english description and trim it") {

    val input = SpeciesDetail(List(Description("          a text      ", language = Language("en"))))

    val actual = input.descriptionFor("en")

    assert(actual.contains("a text"))
  }
}
