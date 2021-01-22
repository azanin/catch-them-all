package api.http.clients

import api.http.clients.data.PokemonResponse.{ Pokemon, Species }
import api.http.clients.data.PokemonSpeciesResponse.{ Description, Language, SpeciesDetail }
import api.http.clients.data.TranslateShakespeareResponse.{ Contents, ShakespeareResponse }
import cats.Applicative
import cats.effect.Sync
import io.circe.Decoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

object Json {
  implicit def deriveEntityEncoder[F[_]: Applicative: Sync, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]

  implicit val speciesDecoder = Decoder.forProduct1("name")(Species.apply)
  implicit val pokemonDecoder = Decoder.forProduct1("species")(Pokemon.apply)

  implicit val languageDecoder      = Decoder.forProduct1("name")(Language.apply)
  implicit val descriptionDecoder   = Decoder.forProduct2("flavor_text", "language")(Description.apply)
  implicit val speciesDetailDecoder = Decoder.forProduct1("flavor_text_entries")(SpeciesDetail.apply)

  implicit val contentsDecoder: Decoder[Contents] = Decoder.forProduct1("translated")(Contents.apply)

  implicit val shakespeareDecoder: Decoder[ShakespeareResponse] =
    Decoder.forProduct1("contents")(ShakespeareResponse.apply)

}
