package api.http.clients

import cats.effect.IO
import org.http4s.Method.POST
import org.http4s.{ Headers, MediaType, Request, Uri, UrlForm }
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import Json._
import api.http.clients.data.TranslateShakespeareResponse.ShakespeareResponse

class TranslateClient private (private val httpClient: Client[IO], url: Uri) {

  def translate(text: String): IO[ShakespeareResponse] = {
    val targetUri = url.withPath(s"/translate/shakespeare.json")

    val request = Request[IO](
      method = POST,
      uri = targetUri,
      headers = Headers.of(`Content-Type`(MediaType.application.`x-www-form-urlencoded`))
    ).withEntity(
      UrlForm(
        "text" -> text
      )
    )

    httpClient.expect[ShakespeareResponse](request)
  }
}

object TranslateClient {
  def apply(client: Client[IO], baseUri: Uri): TranslateClient = new TranslateClient(client, baseUri)
}
