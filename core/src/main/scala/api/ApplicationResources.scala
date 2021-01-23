package api

import api.configurations.Configurations
import cats.effect.{ ContextShift, IO }
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.middleware.Logger

import scala.concurrent.ExecutionContext

case class ApplicationResources(client: Client[IO])

object ApplicationResources {

  def make(implicit cs: ContextShift[IO]) = BlazeClientBuilder[IO](ExecutionContext.global)
    .withConnectTimeout(Configurations.httpConnectTimeout)
    .withRequestTimeout(Configurations.httpRequestTimeout)
    .resource
    .map(client => Logger(logHeaders = true, logBody = true)(client))
    .map(ApplicationResources(_))

}
