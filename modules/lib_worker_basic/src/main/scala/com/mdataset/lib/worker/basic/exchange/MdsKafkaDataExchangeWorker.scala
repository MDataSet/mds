package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.fasterxml.jackson.databind.JsonNode
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsInsertReqDTO, MdsQuerySqlReqDTO, MdsRegisterReqDTO}
import com.mdataset.lib.worker.basic.MdsWorkerBasicContext

object MdsKafkaDataExchangeWorker extends MdsDataExchangeWorker {

  private val insertProducer = KafkaProcessor.Producer(
    BasicContext.FLAG_DATA_INSERT + MdsWorkerBasicContext.source.code,
    BasicContext.FLAG_DATA_INSERT + MdsWorkerBasicContext.source.code)

  private val querySqlReqProducer = KafkaProcessor.Producer(
    BasicContext.FLAG_DATA_QUERY_SQL_REQ + MdsWorkerBasicContext.source.code,
    BasicContext.FLAG_DATA_QUERY_SQL_REQ + MdsWorkerBasicContext.source.code)

  override protected def fetchRegisterReq(source: MdsRegisterReqDTO): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_DATA_REGISTER, source)
    Resp.success(null)
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_DATA_UN_REGISTER, code)
    Resp.success(null)
  }

  override protected def fetchInsert(tableName: String, items: List[String]): Resp[Void] = {
    insertProducer.send(JsonHelper.toJsonString(MdsInsertReqDTO(tableName, items)))
    Resp.success(null)
  }

  override protected def fetchQueryBySqlReq(sql: String, parameters: List[Any]): Resp[JsonNode] = {
    val result = querySqlReqProducer.ack(
      JsonHelper.toJsonString(MdsQuerySqlReqDTO(sql, parameters)),
      BasicContext.FLAG_DATA_QUERY_SQL_RESP + MdsWorkerBasicContext.source.code)
    if (result) {
      Resp.success(JsonHelper.toJson(result.body))
    } else {
      result
    }
  }

}

