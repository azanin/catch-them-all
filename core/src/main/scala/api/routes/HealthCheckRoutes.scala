package api.routes

import api.Endpoints
import cats.effect.{ ContextShift, IO, Timer }
import cats.implicits._
import sttp.tapir.server.http4s.Http4sServerInterpreter

class HealthCheckRoutes(implicit C: ContextShift[IO], T: Timer[IO]) {

  val healtcheckRoute =
    Http4sServerInterpreter.toRoutes(Endpoints.healthcheck)(_ => IO("Up and running".asRight[Unit]))
}

object HealthCheckRoutes {
  def make(implicit C: ContextShift[IO], T: Timer[IO]): HealthCheckRoutes = new HealthCheckRoutes()
}
