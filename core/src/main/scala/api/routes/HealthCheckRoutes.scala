package api.routes

import api.Endpoints
import cats.effect.IO
import cats.implicits._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.effect.Temporal

class HealthCheckRoutes(implicit C: ContextShift[IO], T: Temporal[IO]) {

  val healtcheckRoute =
    Http4sServerInterpreter.toRoutes(Endpoints.healthcheck)(_ => IO("Up and running".asRight[Unit]))
}

object HealthCheckRoutes {
  def make(implicit T: Temporal[IO]): HealthCheckRoutes = new HealthCheckRoutes()
}
