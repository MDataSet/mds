package com.mdataset.service.bd

import com.mdataset.lib.basic.model.MdsRegisterEntityReqDTO
import com.mdataset.service.bd.exchange.MdsDefaultDataExchangeMaster

/**
  * BD Service上下文
  */
object MdsContext {

  // 各Worker注册请求集合
  val sources: collection.mutable.Map[String, MdsRegisterEntityReqDTO] = collection.mutable.Map[String, MdsRegisterEntityReqDTO]()

  // Worker交互实现
  val dataExchangeMaster = MdsDefaultDataExchangeMaster

}