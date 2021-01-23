package api

import sttp.tapir._
import sttp.tapir.json.circe.jsonBody

object Endpoints {
  val healthcheck: Endpoint[Unit, Unit, String, Any] = endpoint.get.in("health").out(jsonBody[String])

  val pokemonEndpoint: Endpoint[String, Unit, Unit, Any] =
    endpoint.get
      .in("pokemon")
      .in(path[String]("pokemonName"))
}
