package com.rhdzmota.bitso.consumer.model.implicits

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import com.rhdzmota.bitso.consumer.model.trades._

object Decoders {
  implicit val decodeSingleTrade: Decoder[SingleTrade]  = deriveDecoder[SingleTrade]
  implicit val decodeTrades: Decoder[Trades]            = deriveDecoder[Trades]
}
