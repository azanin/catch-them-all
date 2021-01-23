package api.http.clients

import cats.effect.IO
import io.circe.Decoder
import org.http4s._
import org.http4s.circe.toMessageSynax
import api.domain.errors.Errors._
import cats.implicits.catsSyntaxApplicativeErrorId

object ResponseHelper {

  def handleError[A: Decoder](response: Response[IO]) =
    response.status match {
      case Status.Ok              => response.asJsonDecode[A]
      case Status.NotFound        => NotFound(response.status.reason).raiseError[IO, A]
      case Status.TooManyRequests => TooManyRequest(response.status.reason).raiseError[IO, A]
      case _                      => ServiceUnavailable(response.status.reason).raiseError[IO, A]
    }
}
