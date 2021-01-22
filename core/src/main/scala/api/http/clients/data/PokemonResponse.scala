package api.http.clients.data

object PokemonResponse {
  case class Pokemon(species: Species)
  case class Species(name: String)
}
