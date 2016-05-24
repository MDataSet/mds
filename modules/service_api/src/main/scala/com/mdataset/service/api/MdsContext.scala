package com.mdataset.service.api

import com.mdataset.lib.basic.model.MdsSourceItemDTO
import com.mdataset.service.api.exchange.MdsEBAPIExchangeMaster

object MdsContext {

  // TODO 持久化
  val sources: collection.mutable.Map[String, Map[String, MdsSourceItemDTO]] = collection.mutable.Map[String, Map[String, MdsSourceItemDTO]]()

  // TODO config
  val apiExchangeMaster=MdsEBAPIExchangeMaster

}
