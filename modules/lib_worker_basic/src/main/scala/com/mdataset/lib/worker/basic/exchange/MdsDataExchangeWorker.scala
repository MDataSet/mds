package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.fasterxml.jackson.databind.JsonNode
import com.mdataset.lib.basic.model.{MdsIdModel, MdsRegisterReqDTO}

import scala.collection.JavaConversions._

trait MdsDataExchangeWorker extends MdsExchangeWorker {

  def registerReq(source: MdsRegisterReqDTO): Unit = {
    fetchRegisterReq(source)
  }

  protected def fetchRegisterReq(source: MdsRegisterReqDTO): Resp[Void]

  def insert[E <: MdsIdModel](lines: List[E]): Resp[Void] = {
    if (lines != null) {
      val items = lines.map {
        line =>
          JsonHelper.toJsonString(line)
      }
      val tableName = lines.head.getClass.getSimpleName
      fetchInsert(tableName, items)
      Resp.success(null)
    } else {
      Resp.success(null)
    }
  }

  protected def fetchInsert(tableName: String, items: List[String]): Resp[Void]

  def queryBySqlReq[E: Manifest](sql: String, parameters: List[Any]): Resp[List[E]] = {
    val result = fetchQueryBySqlReq(sql, parameters)
    if (result) {
      Resp.success(result.body.map(JsonHelper.toObject[E]).toList)
    }else {
      result
    }
  }

  protected def fetchQueryBySqlReq(sql: String, parameters: List[Any]): Resp[JsonNode]

}
