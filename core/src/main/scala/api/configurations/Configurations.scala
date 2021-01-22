package api.configurations

import cats.effect.IO
import org.http4s.Uri
import cats.implicits._

final case class Configurations(basePokemonApiUrl: Uri, baseTranslateApiUrl: Uri)

object Configurations {

  def load: IO[Configurations] = (IO(sys.env("POKEMON_HOST")), IO(sys.env("TRANSLATE_HOST"))).mapN {
    (pokemonUri: String, translateUri: String) =>
      Configurations(Uri.unsafeFromString(pokemonUri), Uri.unsafeFromString(translateUri))
  }

}
