package com.mdataset.lib.worker.basic

import com.mdataset.lib.basic.model.MdsSourceMainDTO
import com.mdataset.lib.worker.basic.exchange.{MdsEBExchangeWorkerAPI, MdsKafkaExchangeDataAPI}

object MdsContext {

  var adapter: MdsAdapter = _
  var source: MdsSourceMainDTO = _

  val defaultExchangeWorkerAPI = MdsEBExchangeWorkerAPI
  val defaultExchangeDataAPI = MdsKafkaExchangeDataAPI

}
