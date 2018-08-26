package com.rhdzmota.bitso.consumer.service.database.impl

import com.rhdzmota.bitso.consumer.model.trades.TradeRow
import com.rhdzmota.bitso.consumer.service.database.Database
import akka.stream.alpakka.cassandra.scaladsl.CassandraSink
import akka.stream.scaladsl.{Flow, Sink}
import akka.{Done, NotUsed}
import com.datastax.driver.core.{BoundStatement, Cluster, PreparedStatement, Session}
import com.rhdzmota.bitso.consumer.conf.{Context, Settings}

import scala.concurrent.Future


case object Cassandra extends Database with Context {

  implicit private val session: Session = Cluster.builder
    .addContactPoint(Settings.Cassandra.address)
    .withPort(Settings.Cassandra.port)
    .build.connect()

  private val query: String =
    s"""
       |INSERT INTO ${Settings.Cassandra.keyspaceName}.${Settings.Cassandra.trades.table}(id, book, received, arrival, amount, rate, value, maker_side_action, maker_order, taker_order)
       |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
     """.stripMargin

  private val preparedStatement: PreparedStatement = session.prepare(query)

  private val statementBinder: (TradeRow, PreparedStatement) => BoundStatement =
    (tradeRow: TradeRow, statement: PreparedStatement) => statement.bind(
      tradeRow.id.asInstanceOf[java.lang.Long],
      tradeRow.book,
      tradeRow.received,
      tradeRow.arrival.asInstanceOf[java.lang.Integer],
      tradeRow.amount.asInstanceOf[java.lang.Double],
      tradeRow.rate.asInstanceOf[java.lang.Double],
      tradeRow.value.asInstanceOf[java.lang.Double],
      tradeRow.makerSideAction,
      tradeRow.makerOrder,
      tradeRow.takerOrder
    )


  private val cassandraSink: Sink[TradeRow, Future[Done]] = CassandraSink[TradeRow](
    Settings.Cassandra.trades.parallelism,
    preparedStatement,
    statementBinder
  )

  override def sink: Sink[TradeRow, NotUsed] = Flow[TradeRow].to(cassandraSink)

}
