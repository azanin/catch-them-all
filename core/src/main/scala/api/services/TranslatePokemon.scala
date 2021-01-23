package api.services

import api.domain.ShakespeareDescription
import api.http.clients.{ PokemonApi, TranslateApi }
import cats.effect.IO
import cats.implicits._

class TranslatePokemon private (
  private val pokemonApiClient: PokemonApi,
  translateApiClient: TranslateApi
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

  def apply(pokemonApiClient: PokemonApi, translateApiClient: TranslateApi) =
    new TranslatePokemon(pokemonApiClient, translateApiClient)
}
