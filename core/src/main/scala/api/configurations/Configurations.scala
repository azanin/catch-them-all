package api.configurations

import cats.effect.IO
import cats.implicits._

import scala.concurrent.duration.{ Duration, DurationInt }
import scala.language.postfixOps

final case class Configurations(basePokemonApiUrl: String, baseTranslateApiUrl: String)

object Configurations {

  val httpRequestTimeout: Duration = 4 seconds
  val httpConnectTimeout: Duration = 5 seconds

  val serverHost = "0.0.0.0"
  val serverPort = 80

  def load: IO[Configurations] = (IO(sys.env("POKEMON_HOST")), IO(sys.env("TRANSLATE_HOST"))).mapN {
    (pokemonUri: String, translateUri: String) =>
      Configurations(pokemonUri, translateUri)
  }

}
