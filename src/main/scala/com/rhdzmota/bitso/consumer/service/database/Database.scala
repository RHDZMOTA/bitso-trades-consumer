package com.rhdzmota.bitso.consumer.service.database

import akka.Done
import akka.stream.scaladsl.Sink
import com.rhdzmota.bitso.consumer.model.trades.TradeRow
import scala.concurrent.Future

trait Database {
  def sink: Sink[TradeRow, Future[Done]]
}
