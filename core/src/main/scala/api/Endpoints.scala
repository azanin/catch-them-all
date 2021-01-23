package api

import api.domain.ShakespeareDescription
import api.domain.errors.Errors.{ ErrorInfo, NotFound, ServiceUnavailable }
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir.generic.auto.schemaForCaseClass

object Endpoints {
  val healthcheck: Endpoint[Unit, Unit, String, Any] = endpoint.get.in("health").out(jsonBody[String])

  val pokemonEndpoint: Endpoint[String, ErrorInfo, ShakespeareDescription, Any] =
    endpoint.get
      .in("pokemon")
      .in(path[String]("pokemonName"))
      .out(jsonBody[ShakespeareDescription])
      .errorOut(
        oneOf[ErrorInfo](
          statusMapping(StatusCode.NotFound, jsonBody[NotFound].description("not found")),
          statusMapping(StatusCode.ServiceUnavailable, jsonBody[ServiceUnavailable].description("service unavailable"))
        )
      )
}
