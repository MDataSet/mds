package com.mdataset.service.bd

import com.mdataset.lib.basic.model.MdsRegisterReqDTO
import com.mdataset.service.bd.exchange.MdsDefaultDataExchangeMaster

/**
  * BD Service上下文
  */
object MdsContext {

  // 各Worker注册请求集合
  // TODO 持久化
  val sources: collection.mutable.Map[String, MdsRegisterReqDTO] = collection.mutable.Map[String, MdsRegisterReqDTO]()

  // Worker交互实现
  // TODO config
  val dataExchangeMaster = MdsDefaultDataExchangeMaster

}