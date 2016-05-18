package com.mdataset.startup

import com.mdataset.lib.basic.model.MdsSourceItemDTO
import com.mdataset.startup.exchange.MdsEBExchangeMasterAPI

object MdsContext {

  val sources: collection.mutable.Map[String, Map[String, MdsSourceItemDTO]] = collection.mutable.Map[String, Map[String, MdsSourceItemDTO]]()

  val defaultExchangeAPI=MdsEBExchangeMasterAPI

}
