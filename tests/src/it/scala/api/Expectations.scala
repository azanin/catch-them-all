package api

import io.circe.literal._

object Expectations {

  def pokemonReponse(speciesNamw: String) = json""" {
        "species": {
        "name": $speciesNamw,
        "url": "https://pokeapi.co/api/v2/pokemon-species/132/"
        }
        }
      """.noSpaces

  def pokemonSpeciesResponse(description: String) = json"""{
    "flavor_text_entries": [{
      "flavor_text": $description,
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
   ]}""".noSpaces

  def shakespeareResponse(translated: String) = json"""{
  "success": {
    "total": 1
  },
  "contents": {
    "translated": $translated,
    "text": "to be translated",
    "translation": "shakespeare"
  }
}""".noSpaces

}
