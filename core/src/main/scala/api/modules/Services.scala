package api.modules

import api.services.TranslatePokemon

case class Services(translatePokemon: TranslatePokemon)

object Services {

  def make(httpClients: HttpClients): Services = new Services(
    TranslatePokemon(httpClients.pokemonClient, httpClients.translatorClient)
  )

}
