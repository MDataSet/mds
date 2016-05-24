package com.mdataset.service.bd.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor.Producer
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsInsertReqDTO, MdsQuerySqlReqDTO, MdsRegisterReqDTO}

object MdsKafkaDataExchangeMaster extends MdsDataExchangeMaster {

  private val producers = collection.mutable.Map[String, collection.mutable.Map[String, Producer]]()

  override protected def fetchRegisterResp(fun: MdsRegisterReqDTO => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[MdsRegisterReqDTO](BasicContext.FLAG_DATA_REGISTER, {
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

  override protected def fetchInsert(code: String, callback: MdsInsertReqDTO => Resp[Void]): Unit = {
    KafkaProcessor.Consumer(BasicContext.FLAG_DATA_INSERT + code, EZContext.module).receive({
      (message, _) =>
        callback(JsonHelper.toObject[MdsInsertReqDTO](message))
    })
  }

  override protected def fetchQueryBySqlResp(code: String, callback: MdsQuerySqlReqDTO => Resp[Any]): Unit = {
    val producer = createAndGetProducer(code, BasicContext.FLAG_DATA_QUERY_SQL_RESP)
    KafkaProcessor.Consumer(BasicContext.FLAG_DATA_QUERY_SQL_REQ + code, EZContext.module).receive({
      (message, messageId) =>
        val resp = callback(JsonHelper.toObject[MdsQuerySqlReqDTO](message))
        if (resp) {
          producer.send(JsonHelper.toJsonString(resp.body), messageId)
        }
        resp
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

