package com.mdataset.lib.worker.basic

import com.mdataset.lib.basic.model.MdsSourceMainDTO
import com.mdataset.lib.worker.basic.exchange.{MdsEBAPIExchangeWorker, MdsKafkaDataExchangeWorker}

object MdsContext {

  var adapter: MdsAdapter = _
  var source: MdsSourceMainDTO = _

  lazy val apiExchangeWorker = MdsEBAPIExchangeWorker
  lazy val dataExchangeWorker = MdsKafkaDataExchangeWorker

}
