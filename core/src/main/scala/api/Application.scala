package api

import cats.effect.{ ExitCode, IO, IOApp }

object Application extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    IO(println("Hellp World")).as(ExitCode.Success)
}
