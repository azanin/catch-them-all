package api.http.clients

import api.http.clients.data.PokemonResponse.Pokemon
import api.http.clients.data.PokemonSpeciesResponse.SpeciesDetail
import cats.effect.IO
import org.http4s._
import org.http4s.client.Client
import ResponseHelper._
import Json._

trait PokemonApi {
  def getPokemon(name: String): IO[Pokemon]

  def getPokemonSpecies(name: String): IO[SpeciesDetail]
}

class PokemonApiClient private (private val httpClient: Client[IO], url: String) extends PokemonApi {

  def getPokemon(name: String): IO[Pokemon] = {
    val pokemonEndpoint = Uri.unsafeFromString(url + s"/pokemon/$name")
    val request         = Request[IO](
      Method.GET,
      pokemonEndpoint
    )

    httpClient.run(request).use(handleError[Pokemon])
  }

  def getPokemonSpecies(name: String): IO[SpeciesDetail] = {
    val speciesPokemonEndpoint = Uri.unsafeFromString(url + s"/pokemon-species/$name")
    val request                = Request[IO](
      Method.GET,
      speciesPokemonEndpoint
    )

    httpClient.run(request).use(handleError[SpeciesDetail])
  }

}

object PokemonApiClient {
  def apply(client: Client[IO], baseUri: String): PokemonApiClient = new PokemonApiClient(client, baseUri)
}
