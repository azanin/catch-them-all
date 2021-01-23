package api.http.clients.data

object PokemonSpeciesResponse {

  case class SpeciesDetail(descriptions: List[Description]) {

    def descriptionFor(language: String) =
      descriptions
        .find(_.language.name == language)
        .map(_.text)
        .map(_.trim)
  }
  case class Description(text: String, language: Language)
  case class Language(name: String)
}
