package api.http.clients.data

object TranslateShakespeareResponse {

  case class Contents(translated: String)
  case class ShakespeareResponse(contents: Contents)

}
