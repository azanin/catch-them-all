package api

import cats.effect.testing.scalatest.AsyncIOSpec
import cats.effect.{ Blocker, IO }
import com.dimafeng.testcontainers.{ Container, ForAllTestContainer, MultipleContainers }
import org.http4s.Method.GET
import org.http4s.Status.{ BadRequest, InternalServerError, NotFound, Ok, ServiceUnavailable, TooManyRequests }
import org.http4s.{ Request, Uri }
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.scalatest.freespec.AsyncFreeSpec

class ApplicationIT extends AsyncFreeSpec with ForAllTestContainer with AsyncIOSpec {

  val blocker: Blocker = Blocker.liftExecutionContext(executionContext)

  override def container: Container = MultipleContainers(
    Resources.mockServer,
    Resources.apiContainer
  )

  "Happy path: 200 status" in {

    val actual = Resources.clients(blocker).use { case (httpClient, mockServerClient) =>
      val pokemoneName          = "ditto"
      val speciesName           = "ditto"
      val description           = "a simple description"
      val translatedDescription = "translated description"

      val expectations =
        for {
          exp1 <- IO(
                    mockServerClient
                      .when(request().withPath("/pokemon/" + pokemoneName))
                      .respond(response().withBody(Expectations.pokemonReponse(speciesName)))
                  )
          exp2 <- IO(
                    mockServerClient
                      .when(request().withPath("/pokemon-species/" + speciesName))
                      .respond(response().withBody(Expectations.pokemonSpeciesResponse(description)))
                  )
          exp3 <- IO(
                    mockServerClient
                      .when(request().withPath("/translate/shakespeare.json"))
                      .respond(response().withBody(Expectations.shakespeareResponse(translatedDescription)))
                  )
        } yield (exp1, exp2, exp3)

      val httpRequest: Request[IO] = Request(
        method = GET,
        uri = Uri.unsafeFromString(
          s"http://${Resources.apiContainer.container.getHost}:${Resources.apiContainer.mappedPort(80)}/pokemon/$pokemoneName"
        )
      )

      for {
        _        <- expectations
        response <- httpClient.run(httpRequest).use(r => IO(r))
      } yield response
    }

    actual.asserting(response => assert(response.status == Ok))
  }

  "Wrong pokemon name: 400 status" in {

    val actual = Resources.clients(blocker).use { case (httpClient, _) =>
      val badParameter = -2

      val httpRequest: Request[IO] = Request(
        method = GET,
        uri = Uri.unsafeFromString(
          s"http://${Resources.apiContainer.container.getHost}:${Resources.apiContainer.mappedPort(80)}/pokemon/$badParameter"
        )
      )

      for {
        response <- httpClient.run(httpRequest).use(r => IO(r))
      } yield response
    }

    actual.asserting(response => assert(response.status == BadRequest))
  }

  "Not existing pokemon: return 404 status" in {

    val actual = Resources.clients(blocker).use { case (httpClient, mockServerClient) =>
      val pokemoneName = "notexisting"

      val expectations =
        for {
          exp1 <- IO(
                    mockServerClient
                      .when(request().withPath("/pokemon/" + pokemoneName))
                      .respond(response().withStatusCode(404))
                  )
        } yield exp1

      val httpRequest: Request[IO] = Request(
        method = GET,
        uri = Uri.unsafeFromString(
          s"http://${Resources.apiContainer.container.getHost}:${Resources.apiContainer.mappedPort(80)}/pokemon/$pokemoneName"
        )
      )

      for {
        _        <- expectations
        response <- httpClient.run(httpRequest).use(r => IO(r))
      } yield response
    }

    actual.asserting(response => assert(response.status == NotFound))
  }

  "Internal Server error: not parsable payload 500 status" in {

    val actual = Resources.clients(blocker).use { case (httpClient, mockServerClient) =>
      val pokemoneName = "ditto"

      val expectations =
        for {
          exp1 <- IO(
                    mockServerClient
                      .when(request().withPath("/pokemon/" + pokemoneName))
                      .respond(response().withBody("not a json"))
                  )
        } yield exp1

      val httpRequest: Request[IO] = Request(
        method = GET,
        uri = Uri.unsafeFromString(
          s"http://${Resources.apiContainer.container.getHost}:${Resources.apiContainer.mappedPort(80)}/pokemon/$pokemoneName"
        )
      )

      for {
        _        <- expectations
        response <- httpClient.run(httpRequest).use(r => IO(r))
      } yield response
    }

    actual.asserting(response => assert(response.status == InternalServerError))
  }

  "Service unavailable: errors from dependent apis return 503 status" in {

    val actual = Resources.clients(blocker).use { case (httpClient, mockServerClient) =>
      val pokemoneName = "ditto"

      val expectations =
        for {
          exp1 <- IO(
                    mockServerClient
                      .when(request().withPath("/pokemon/" + pokemoneName))
                      .respond(response().withStatusCode(500))
                  )
        } yield exp1

      val httpRequest: Request[IO] = Request(
        method = GET,
        uri = Uri.unsafeFromString(
          s"http://${Resources.apiContainer.container.getHost}:${Resources.apiContainer.mappedPort(80)}/pokemon/$pokemoneName"
        )
      )

      for {
        _        <- expectations
        response <- httpClient.run(httpRequest).use(r => IO(r))
      } yield response
    }

    actual.asserting(response => assert(response.status == ServiceUnavailable))
  }

  "Too many requests: too many requesto to a dependent service return 429 status" in {

    val actual = Resources.clients(blocker).use { case (httpClient, mockServerClient) =>
      val pokemoneName = "ditto"

      val expectations =
        for {
          exp1 <- IO(
                    mockServerClient
                      .when(request().withPath("/pokemon/" + pokemoneName))
                      .respond(response().withStatusCode(429))
                  )
        } yield exp1

      val httpRequest: Request[IO] = Request(
        method = GET,
        uri = Uri.unsafeFromString(
          s"http://${Resources.apiContainer.container.getHost}:${Resources.apiContainer.mappedPort(80)}/pokemon/$pokemoneName"
        )
      )

      for {
        _        <- expectations
        response <- httpClient.run(httpRequest).use(r => IO(r))
      } yield response
    }

    actual.asserting(response => assert(response.status == TooManyRequests))
  }

}
