package api.http.clients.data

object PokemonSpeciesResponse {
  case class SpeciesDetail(descriptions: List[Description])
  case class Description(text: String, language: Language)
  case class Language(name: String)
}
