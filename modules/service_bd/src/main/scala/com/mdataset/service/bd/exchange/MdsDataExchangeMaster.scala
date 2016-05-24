package com.mdataset.service.bd.exchange

import java.util.concurrent.CopyOnWriteArraySet

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsInsertReqDTO, MdsQuerySqlReqDTO, MdsRegisterReqDTO}
import com.mdataset.service.bd.MdsContext
import com.typesafe.scalalogging.slf4j.LazyLogging

trait MdsDataExchangeMaster extends LazyLogging {

  private val isInit = new CopyOnWriteArraySet[String]()

  def registerResp(): Unit = {
    fetchRegisterResp({
      source =>
        val code = source.code
        MdsContext.sources += code -> source
        registerEntityMeta(source)
        insert(code)
        queryBySqlResp(code)
        Resp.success(null)
    })
  }

  protected def fetchRegisterResp(fun: MdsRegisterReqDTO => Resp[Void]): Unit

  private def registerEntityMeta(source: MdsRegisterReqDTO): Unit = {
    // TODO create table
  }

  def unRegisterResp(): Unit = {
    fetchUnRegisterResp({
      code =>
        MdsContext.sources -= code
        Resp.success(null)
    })
  }


  protected def fetchUnRegisterResp(fun: String => Resp[Void]): Unit

  def insert(code: String): Unit = {
    synchronized {
      if (!isInit.contains("insert_" + code)) {
        fetchInsert(code, {
          insertReq =>
            // TODO save
            Resp.success(null)
        })
        isInit.add("insert_" + code)
      }
    }
  }

  protected def fetchInsert(code: String, callback: MdsInsertReqDTO => Resp[Void]): Unit

  def queryBySqlResp(code: String): Unit = {
    synchronized {
      if (!isInit.contains("queryBySqlResp_" + code)) {
        fetchQueryBySqlResp(code, {
          querySqlReq =>
            // TODO query
            Resp.success(s"""[{"name":"a"}]""")
        })
        isInit.add("queryBySqlResp_" + code)
      }
    }
  }

  protected def fetchQueryBySqlResp(code: String, callback: MdsQuerySqlReqDTO => Resp[Any]): Unit

}
