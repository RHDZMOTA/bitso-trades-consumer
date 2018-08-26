package com.rhdzmota.bitso.consumer.service.database

import akka.NotUsed
import akka.stream.scaladsl.Sink
import com.rhdzmota.bitso.consumer.model.trades.TradeRow

trait Database {
  def sink: Sink[TradeRow, NotUsed]
}
