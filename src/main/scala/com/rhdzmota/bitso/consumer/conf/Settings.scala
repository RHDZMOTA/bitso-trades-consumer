package com.rhdzmota.bitso.consumer.conf

import com.typesafe.config.{Config, ConfigFactory}

object Settings {
  private val app: Config = ConfigFactory.load().getConfig("application")

  object Cassandra {
    private val cassandra: Config = app.getConfig("cassandra")
    case class TableObj(name: String) {
      private val config: Config = cassandra.getConfig(name)
      val table: String = config.getString("table")
      val parallelism: Int = config.getInt("parallelism")
    }
    val address: String = cassandra.getString("address")
    val port: Int       = cassandra.getInt("port")
    val keyspaceName: String  = cassandra.getString("keyspaceName")
    val trades: TableObj = TableObj("trades")
  }

  object Bitso {
    private val bitso: Config = app.getConfig("bitso")
    val ws: String = bitso.getString("ws")

    object Subscription {
      private val subs: Config = bitso.getConfig("subscription")

      object Json {
        private val json: Config = subs.getConfig("json")
        val string: String = json.getString("string")
        object Replace {
          private val replace: Config = json.getConfig("replace")
          val book: String = replace.getString("book")
          val `type`: String = replace.getString("type")
        }
      }

      object Labels {
        private val labels: Config = subs.getConfig("labels")
        val orders: String = labels.getString("orders")
        val trades: String = labels.getString("trades")
        val diff: String = labels.getString("diff")
      }
    }

    object Coins {
      private val coins: Config = bitso.getConfig("coins")
      case class BaseCoin(name: String, units: String) {
        private val baseConfig: Config = coins.getConfig(name)
        case class UnitsCoin() {
          private val unitsConfig: Config = baseConfig.getConfig(units)
          private val label: String = unitsConfig.getString("label")
          private val jsonString: String = Subscription.Json.string
            .replace(Subscription.Json.Replace.book, label)
          val orders: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.orders)
          val trades: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.trades)
          val diff: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.diff)
        }
        val exchange: UnitsCoin = UnitsCoin()
      }


      val btcmxn: BaseCoin = BaseCoin("btc", "mxn")

      object Btc {
        private val btc: Config = coins.getConfig("btc")
        object Mxn {
          private val mxn: Config = btc.getConfig("mxn")
          private val label: String = mxn.getString("label")
          private val jsonString: String = Subscription.Json.string
            .replace(Subscription.Json.Replace.book, label)
          val orders: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.orders)
          val trades: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.trades)
          val diff: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.diff)
        }
      }
      object Eth {
        private val eth: Config = coins.getConfig("eth")
        object Mxn {
          private val mxn: Config = eth.getConfig("mxn")
          private val label: String = mxn.getString("label")
          private val jsonString: String = Subscription.Json.string
            .replace(Subscription.Json.Replace.book, label)
          val orders: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.orders)
          val trades: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.trades)
          val diff: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.diff)
        }
      }
      object Xrp {
        private val xrp: Config = coins.getConfig("xrp")
        object Mxn {
          private val mxn: Config = xrp.getConfig("mxn")
          private val label: String = mxn.getString("label")
          private val jsonString: String = Subscription.Json.string
            .replace(Subscription.Json.Replace.book, label)
          val orders: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.orders)
          val trades: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.trades)
          val diff: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.diff)
        }
      }
      object Ltc {
        private val ltc: Config = coins.getConfig("ltc")
        object Mxn {
          private val mxn: Config = ltc.getConfig("mxn")
          private val label: String = mxn.getString("label")
          private val jsonString: String = Subscription.Json.string
            .replace(Subscription.Json.Replace.book, label)
          val orders: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.orders)
          val trades: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.trades)
          val diff: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.diff)
        }
      }
      object Bch {
        private val bch: Config = coins.getConfig("bch")
        object Mxn {
          private val mxn: Config = bch.getConfig("mxn")
          private val label: String = mxn.getString("label")
          private val jsonString: String = Subscription.Json.string
            .replace(Subscription.Json.Replace.book, label)
          val orders: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.orders)
          val trades: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.trades)
          val diff: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.diff)
        }
      }
      object Tusd {
        private val tusd: Config = coins.getConfig("tusd")
        object Mxn {
          private val mxn: Config = tusd.getConfig("mxn")
          private val label: String = mxn.getString("label")
          private val jsonString: String = Subscription.Json.string
            .replace(Subscription.Json.Replace.book, label)
          val orders: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.orders)
          val trades: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.trades)
          val diff: String = jsonString
            .replace(Subscription.Json.Replace.`type`, Subscription.Labels.diff)
        }
      }
    }


  }
}