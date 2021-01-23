package api.domain.errors

import scala.util.control.NoStackTrace

object Errors {
  sealed trait ErrorInfo                       extends NoStackTrace
  case class ServiceUnavailable(text: String)  extends ErrorInfo
  case class NotFound(text: String)            extends ErrorInfo
  case class InternalServerError(text: String) extends ErrorInfo
  case class TooManyRequest(text: String)      extends ErrorInfo
}
