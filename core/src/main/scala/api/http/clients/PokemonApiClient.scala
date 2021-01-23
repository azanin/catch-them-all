package api.http.clients

import api.http.clients.data.PokemonResponse.Pokemon
import api.http.clients.data.PokemonSpeciesResponse.SpeciesDetail
import cats.effect.IO
import org.http4s.Uri
import org.http4s.client.Client
import Json._

trait PokemonApi {
  def getPokemon(name: String): IO[Pokemon]

  def getPokemonSpecies(name: String): IO[SpeciesDetail]
}

class PokemonApiClient private (private val httpClient: Client[IO], url: Uri) extends PokemonApi {

  def getPokemon(name: String): IO[Pokemon] = {
    val pokemonEndpoint = url.withPath(s"/pokemon/$name")
    httpClient.expect[Pokemon](pokemonEndpoint)
  }

  def getPokemonSpecies(name: String): IO[SpeciesDetail] = {
    val speciesPokemonEndpoint = url.withPath(s"/pokemon-species/$name")
    httpClient.expect[SpeciesDetail](speciesPokemonEndpoint)
  }

}

object PokemonApiClient {
  def apply(client: Client[IO], baseUri: Uri): PokemonApiClient = new PokemonApiClient(client, baseUri)
}
