package com.mdataset.lib.core.exchange

import com.ecfront.common.{JsonHelper, Resp}

trait MdsExchangeDataAPI {

  def insert(itemCode: String, lines: List[Any]): Resp[Void] = {
    fetchInsert(itemCode, lines)
  }

  protected def fetchInsert(itemCode: String, lines: List[Any]): Resp[Void]

  def queryBySql[E: Manifest](itemCode: String, sql: String, parameters: Map[String, Any]): Resp[List[E]] = {
    val result = fetchQueryBySql(itemCode, sql, parameters)
    if (result) {
      Resp.success(result.body.map(JsonHelper.toObject[E]))
    }
    result
  }

  protected def fetchQueryBySql(itemCode: String, sql: String, parameters: Map[String, Any]): Resp[List[String]]

}
