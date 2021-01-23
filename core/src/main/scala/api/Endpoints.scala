package api

import api.domain.ShakespeareDescription
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import io.circe.generic.auto._
import sttp.tapir.generic.auto.schemaForCaseClass

object Endpoints {
  val healthcheck: Endpoint[Unit, Unit, String, Any] = endpoint.get.in("health").out(jsonBody[String])

  val pokemonEndpoint =
    endpoint.get
      .in("pokemon")
      .in(path[String]("pokemonName"))
      .out(jsonBody[ShakespeareDescription])
}
