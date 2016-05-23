package com.mdataset.service.bd.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor.Producer
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.MdsSourceMainDTO

object MdsKafkaDataExchangeMaster extends MdsDataExchangeMaster {

  private val producers = collection.mutable.Map[String, collection.mutable.Map[String, Producer]]()

  override protected def fetchRegisterResp(fun: MdsSourceMainDTO => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[MdsSourceMainDTO](BasicContext.FLAG_DATA_REGISTER, {
      (source, reply) =>
        reply(fun(source))
    })
  }

  override protected def fetchUnRegisterResp(fun: String => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[String](BasicContext.FLAG_DATA_UN_REGISTER, {
      (code, reply) =>
        reply(fun(code))
    })
  }

  override protected def fetchInsert(code: String, itemCode: String, callback: List[String] => Resp[Void]): Unit = {
    KafkaProcessor.Consumer(BasicContext.FLAG_DATA_INSERT + code + "_" + itemCode, EZContext.module).receive({
      (message, _) =>
        callback(JsonHelper.toObject[List[String]](message))
    })
  }

  override protected def fetchQueryBySqlResp(code: String, itemCode: String, callback: (String, Map[String, Any]) => Resp[Any]): Unit = {
    val producer = createAndGetProducer(code, itemCode, BasicContext.FLAG_DATA_QUERY_SQL_RESP)
    KafkaProcessor.Consumer(BasicContext.FLAG_DATA_QUERY_SQL_REQ + code + "_" + itemCode, EZContext.module).receive({
      (message, messageId) =>
        val resp = callback(JsonHelper.toObject[List[String]](message))
        if (resp) {
          producer.send(resp.body, messageId)
        } else {
          resp
        }
    })
  }

  private def createAndGetProducer(code: String, itemCode: String, topicPrefix: String): Producer = {
    if (!producers.contains(itemCode)) {
      producers += itemCode -> collection.mutable.Map[String, Producer]()
    }
    if (!producers(itemCode).contains(topicPrefix)) {
      producers(itemCode) +=
        topicPrefix -> KafkaProcessor.Producer(topicPrefix + code + "_" + itemCode, EZContext.module)
    }
    producers(itemCode)(topicPrefix)
  }
}

