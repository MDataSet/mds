package com.mdataset.service.bd

import com.mdataset.lib.basic.model.MdsRegisterReqDTO
import com.mdataset.service.bd.exchange.MdsKafkaDataExchangeMaster

object MdsContext {

  // TODO 持久化
  val sources: collection.mutable.Map[String, MdsRegisterReqDTO] = collection.mutable.Map[String, MdsRegisterReqDTO]()


  // TODO config
  val dataExchangeMaster = MdsKafkaDataExchangeMaster

}