package com.mdataset.service.bd.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor.Producer
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsInsertReqDTO, MdsRegisterEntityReqDTO}

object MdsDefaultDataExchangeMaster extends MdsDataExchangeMaster {

  private val producers = collection.mutable.Map[String, collection.mutable.Map[String, Producer]]()

  override protected def fetchRegisterResp(callback: MdsRegisterEntityReqDTO => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[MdsRegisterEntityReqDTO](BasicContext.FLAG_DATA_REGISTER, {
      (source, reply) =>
        reply(callback(source))
    })
  }

  override protected def fetchUnRegisterResp(callback: String => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[String](BasicContext.FLAG_DATA_UN_REGISTER, {
      (code, reply) =>
        reply(callback(code))
    })
  }

  override protected def fetchInsertResp(code: String, callback: MdsInsertReqDTO => Resp[Void]): Unit = {
    KafkaProcessor.Consumer(BasicContext.FLAG_DATA_INSERT + code, EZContext.module).receive({
      (message, _) =>
        val insertReq = JsonHelper.toObject[MdsInsertReqDTO](message)
        callback(insertReq)
    })
  }

  private def createAndGetProducer(code: String, topicPrefix: String): Producer = {
    if (!producers.contains(code)) {
      producers += code -> collection.mutable.Map[String, Producer]()
    }
    if (!producers(code).contains(topicPrefix)) {
      producers(code) +=
        topicPrefix -> KafkaProcessor.Producer(topicPrefix + code, EZContext.module)
    }
    producers(code)(topicPrefix)
  }

}

