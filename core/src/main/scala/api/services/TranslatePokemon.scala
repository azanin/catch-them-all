package api.services

import api.domain.ShakespeareDescription
import api.http.clients.{ PokemonApiClient, TranslateApiClient }
import cats.effect.IO
import cats.implicits._

class TranslatePokemon private (
  private val pokemonApiClient: PokemonApiClient,
  translateApiClient: TranslateApiClient
) {

  def translateDescriptionOf(name: String): IO[Option[ShakespeareDescription]] =
    for {
      pokemon         <- pokemonApiClient.getPokemon(name)
      species         <- pokemonApiClient.getPokemonSpecies(pokemon.species.name)
      text             = species.descriptionFor("en")
      shakespeareText <- text.map(translateApiClient.translate).sequence
    } yield shakespeareText.map(response => ShakespeareDescription(response.contents.translated))

}

object TranslatePokemon {

  def apply(pokemonApiClient: PokemonApiClient, translateApiClient: TranslateApiClient) =
    new TranslatePokemon(pokemonApiClient, translateApiClient)
}
