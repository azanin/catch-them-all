package api.http.clients

import cats.effect.IO
import org.http4s.Method.POST
import org.http4s.{ Headers, MediaType, Request, Uri, UrlForm }
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import Json._
import api.http.clients.data.TranslateShakespeareResponse.ShakespeareResponse
import ResponseHelper._

trait TranslateApi {
  def translate(text: String): IO[ShakespeareResponse]
}

class TranslateApiClient private (private val httpClient: Client[IO], url: Uri) extends TranslateApi {

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

    httpClient.run(request).use(handleError[ShakespeareResponse])
  }
}

object TranslateApiClient {
  def apply(client: Client[IO], baseUri: Uri): TranslateApiClient = new TranslateApiClient(client, baseUri)
}
