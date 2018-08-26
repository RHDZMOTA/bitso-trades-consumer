package com.rhdzmota.bitso.consumer.service.bitso

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import com.rhdzmota.bitso.consumer.conf.{Context, Settings}
import com.rhdzmota.bitso.consumer.model.trades.{TradeRow, Trades}

import scala.concurrent.Promise

case object Bitso extends Context {

  val tradesSource: Source[Message, Promise[Option[Message]]] = Source(List(
    TextMessage(Settings.Bitso.Coins.btcmxn.exchange.trades),
    TextMessage(Settings.Bitso.Coins.Eth.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Xrp.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Ltc.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Bch.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Tusd.Mxn.trades),
  )).concatMat(Source.maybe[Message])(Keep.right)

  def tradesSink(databaseSink: Sink[TradeRow, NotUsed]): Sink[Message, NotUsed] =
    Flow[Message]
      .map({
        case message: TextMessage.Strict  => Trades.fromString(message.text)
        case _                            => Left(Trades.Errors.NotTextMessage)})
      .mapConcat({
        case Right(trades)  => trades.toTradeRow
        case _              => List[TradeRow]()
      })
      .to(databaseSink)
}
