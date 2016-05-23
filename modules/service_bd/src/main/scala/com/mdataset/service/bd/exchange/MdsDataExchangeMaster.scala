package com.mdataset.service.bd.exchange

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.MdsSourceMainDTO
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.process.{MdsCollectExecScheduleJob, MdsLimitProcessor}
import com.mdataset.service.bd.MdsContext
import com.typesafe.scalalogging.slf4j.LazyLogging

trait MdsDataExchangeMaster extends LazyLogging {

  def registerResp(): Unit = {
    fetchRegisterResp({
      source =>
        val code = source.code
        MdsContext.sources += code -> source.items.map(i => i.item_code -> i).toMap
        MdsContext.sources(code).values.foreach {
          item =>
            val itemCode = item.item_code
            insert(code, itemCode)
            queryBySqlResp(code, itemCode)
        }
        Resp.success(null)
    })
  }

  protected def fetchRegisterResp(fun: MdsSourceMainDTO => Resp[Void]): Unit

  def unRegisterResp(): Unit = {
    fetchUnRegisterResp({
      code =>
        MdsContext.sources -= code
        Resp.success(null)
    })
  }

  protected def fetchUnRegisterResp(fun: String => Resp[Void]): Unit

  def insert(code: String, itemCode: String): Unit = {
    fetchInsert(code,itemCode,{
      lines =>
      // TODO save
        Resp.success(null)
    })
  }

  protected def fetchInsert(code: String, itemCode: String, callback: List[String] => Resp[Void]): Unit

  def queryBySqlResp(code: String, itemCode: String): Unit = {
    fetchQueryBySqlResp(code,itemCode,{
      (sql,parameters) =>
        // TODO query
        Resp.success(null)
    })
  }

  protected def fetchQueryBySqlResp(code: String, itemCode: String, callback: (String,Map[String,Any]) => Resp[Any]): Unit

}
