package com.rhdzmota.bitso.consumer

import akka.http.scaladsl.model.ws.WebSocketUpgradeResponse
import akka.http.scaladsl.Http
import akka.stream.scaladsl.{Flow, Keep}
import akka.http.scaladsl.model.ws.{Message, WebSocketRequest}
import com.rhdzmota.bitso.consumer.service.database.impl.Cassandra
import com.rhdzmota.bitso.consumer.conf.{Context, Settings}
import com.rhdzmota.bitso.consumer.service.bitso.Bitso
import com.rhdzmota.bitso.consumer.service.database.Database

import scala.concurrent.{Future, Promise}

object App extends Context {

  def main(args: Array[String]): Unit = {

    // Services
    val database: Database = Cassandra
    val bitso: Bitso.type = Bitso

    // Define websocket flow
    val webSocketFlow: Flow[Message, Message, Promise[Option[Message]]] =
      Flow.fromSinkAndSourceMat(bitso.tradesSink(database.sink), bitso.tradesSource)(Keep.right)

    // Create request and run
    val (upgradeResponse: Future[WebSocketUpgradeResponse], promise: Promise[Option[Message]] )=
      Http().singleWebSocketRequest(WebSocketRequest(Settings.Bitso.ws), webSocketFlow)

    upgradeResponse.onComplete(println)
  }
}
