package com.mdataset.service.bd

import com.mdataset.lib.basic.model.MdsSourceItemDTO
import com.mdataset.service.bd.exchange.MdsKafkaDataExchangeMaster

object MdsContext {

  // TODO 持久化
  val sources: collection.mutable.Map[String, Map[String, MdsSourceItemDTO]] = collection.mutable.Map[String, Map[String, MdsSourceItemDTO]]()


  // TODO config
  val defaultDataExchangeMaster = MdsKafkaDataExchangeMaster

}