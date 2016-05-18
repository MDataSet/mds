package com.mdataset.lib.core

import com.mdataset.lib.basic.model.MdsSourceMainDTO
import com.mdataset.lib.core.exchange.MdsEBExchangeWorkerAPI

object MdsContext {

  var adapter: MdsAdapter = _
  var source: MdsSourceMainDTO = _

  val defaultExchangeAPI = MdsEBExchangeWorkerAPI


}
