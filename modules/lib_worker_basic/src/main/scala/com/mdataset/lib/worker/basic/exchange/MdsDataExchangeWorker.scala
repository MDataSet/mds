package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.{JsonHelper, Resp}

trait MdsDataExchangeWorker extends MdsExchangeWorker {

  def insert(itemCode: String, lines: List[Any]): Resp[Void] = {
    fetchInsert(itemCode, lines)
  }

  protected def fetchInsert(itemCode: String, lines: List[Any]): Resp[Void]

  def queryBySqlReq[E: Manifest](itemCode: String, sql: String, parameters: Map[String, Any]): Resp[List[E]] = {
    val result = fetchQueryBySqlReq(itemCode, sql, parameters)
    if (result) {
      Resp.success(result.body.map(JsonHelper.toObject[E]))
    }
    result
  }

  protected def fetchQueryBySqlReq(itemCode: String, sql: String, parameters: Map[String, Any]): Resp[List[String]]

}
