package com.mdataset.service.api

import com.ecfront.ez.framework.core.EZContext
import com.mdataset.lib.basic.model.MdsSourceItemDTO
import com.mdataset.service.api.exchange.MdsDefaultAPIExchangeMaster

/**
  * API Service上下文
  */
object MdsContext {

  // 各Worker数据源集合
  // TODO 持久化
  val sources: collection.mutable.Map[String, Map[String, MdsSourceItemDTO]] = collection.mutable.Map[String, Map[String, MdsSourceItemDTO]]()

  // Worker交互实现
  // TODO config
  val apiExchangeMaster = MdsDefaultAPIExchangeMaster

  val socketPort = EZContext.args.getJsonObject("socket").getInteger("port")
  val socketHost = EZContext.args.getJsonObject("socket").getString("host")
}
