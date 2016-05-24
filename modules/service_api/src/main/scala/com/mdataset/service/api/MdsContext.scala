package com.mdataset.service.api

import com.mdataset.lib.basic.model.MdsSourceItemDTO
import com.mdataset.service.api.exchange.MdsEBAPIExchangeMaster

/**
  * API Service上下文
  */
object MdsContext {

  // 各Worker数据源集合
  // TODO 持久化
  val sources: collection.mutable.Map[String, Map[String, MdsSourceItemDTO]] = collection.mutable.Map[String, Map[String, MdsSourceItemDTO]]()

  // Worker交互实现
  // TODO config
  val apiExchangeMaster=MdsEBAPIExchangeMaster

}
