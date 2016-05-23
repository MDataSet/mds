package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor.Producer
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsSourceMainDTO, QuerySqlReq}
import com.mdataset.lib.worker.basic.MdsContext

object MdsKafkaDataExchangeWorker extends MdsDataExchangeWorker {

  private val producers = collection.mutable.Map[String, collection.mutable.Map[String, Producer]]()

  override protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_DATA_REGISTER, source)
    Resp.success(null)
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_DATA_UN_REGISTER, code)
    Resp.success(null)
  }

  override protected def fetchInsert(itemCode: String, lines: List[Any]): Resp[Void] = {
    val producer = createAndGetProducer(itemCode, BasicContext.FLAG_DATA_INSERT)
    if (lines != null) {
      lines.foreach {
        line =>
          producer.send(JsonHelper.toJsonString(line))
      }
      Resp.success(null)
    } else {
      Resp.success(null)
    }
  }

  override protected def fetchQueryBySqlReq(itemCode: String, sql: String, parameters: Map[String, Any]): Resp[List[String]] = {
    val producer = createAndGetProducer(itemCode, BasicContext.FLAG_DATA_QUERY_SQL_REQ)
    val result = producer.ack(JsonHelper.toJsonString(QuerySqlReq(sql, parameters)), BasicContext.FLAG_DATA_QUERY_SQL_RESP + MdsContext.source.code + "_" + itemCode)
    if (result) {
      Resp.success(JsonHelper.toObject[List[String]](result.body))
    } else {
      result
    }
  }

  private def createAndGetProducer(itemCode: String, topicPrefix: String): Producer = {
    if (!producers.contains(itemCode)) {
      producers += itemCode -> collection.mutable.Map[String, Producer]()
    }
    if (!producers(itemCode).contains(topicPrefix)) {
      producers(itemCode) +=
        topicPrefix -> KafkaProcessor.Producer(topicPrefix + MdsContext.source.code + "_" + itemCode, MdsContext.source.code + "_" + itemCode)
    }
    producers(itemCode)(topicPrefix)
  }

}

