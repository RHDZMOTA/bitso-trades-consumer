package com.rhdzmota.bitso.consumer.service.bitso

import java.sql.Timestamp
import java.time.LocalDateTime

import akka.Done
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.rhdzmota.bitso.consumer.conf.{Context, Settings}
import com.rhdzmota.bitso.consumer.model.trades.{TradeRow, Trades}

import scala.concurrent.{Future, Promise}

case object Bitso extends Context {

  val tradesSource: Source[Message, Promise[Option[Message]]] = Source(List(
    TextMessage(Settings.Bitso.Coins.btcmxn.exchange.trades),
    TextMessage(Settings.Bitso.Coins.Eth.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Xrp.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Ltc.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Bch.Mxn.trades),
    TextMessage(Settings.Bitso.Coins.Tusd.Mxn.trades),
  )).concatMat(Source.maybe[Message])(Keep.right)

  def tradesSink(databaseSink: Sink[TradeRow, Future[Done]]): Sink[Message, Future[Done]] =
    Flow[Message]
      .map({
        case message: TextMessage.Strict  => Trades.fromString(message.text)
        case _                            => Left(Trades.Errors.NotTextMessage)})
      .mapConcat({
        case Right(trades)  => trades.toTradeRow
        case Left(error)    => error match {
          case Trades.Errors.NotTextMessage   =>
            val t = Timestamp.valueOf(LocalDateTime.now()).toString
            println(s"[Error][NotTextMessage] $t The object received is not a TextMessage.String")
          case Trades.Errors.CirceError(_, s) =>
            val t = Timestamp.valueOf(LocalDateTime.now()).toString
            println(s"[Error][Circe] $t $s")
        }
          List[TradeRow]()
      })
      .toMat(databaseSink)(Keep.right)
}
