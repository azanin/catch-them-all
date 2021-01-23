package api.modules

import api.ApplicationResources
import api.configurations.Configurations
import api.http.clients.{ PokemonApi, PokemonApiClient, TranslateApi, TranslateApiClient }

object HttpClients {

  def apply(resources: ApplicationResources, configurations: Configurations): HttpClients = new HttpClients {
    override def pokemonClient: PokemonApi = PokemonApiClient(resources.client, configurations.basePokemonApiUrl)

    override def translatorClient: TranslateApi =
      TranslateApiClient(resources.client, configurations.baseTranslateApiUrl)
  }
}

trait HttpClients {
  def pokemonClient: PokemonApi
  def translatorClient: TranslateApi
}
