package com.mdataset.lib.core.exchange

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}
import com.mdataset.lib.core.MdsContext
import com.typesafe.scalalogging.slf4j.LazyLogging


trait MdsExchangeWorkerAPI extends LazyLogging {

  def registerReq(source: MdsSourceMainDTO): Unit = {
    fetchRegisterReq(source)
  }

  protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void]

  def unRegisterReq(code: String): Unit = {
    fetchUnRegisterReq(code)
  }

  protected def fetchUnRegisterReq(code: String): Resp[Void]

  def collectExecResp(code: String): Unit = {
    fetchCollectExecResp(code, {
      (status, reply) =>
        try {
          val result = MdsContext.adapter.collectExec(status.item_code, MdsContext.source.items.find(_.item_code == status.item_code).get, status)
          reply(result)
          if (!result) {
            logger.error(s"Collect Exec error [${result.code}]:${result.message}")
          }
        } catch {
          case e: Throwable =>
            logger.error(s"Collect Exec error", e)
        }
    })
  }

  protected def fetchCollectExecResp(code: String, callback: (MdsCollectStatusDTO, Resp[MdsCollectStatusDTO] => Unit) => Unit): Unit

  def collectTestResp(code: String): Unit = {
    fetchCollectTestResp(code, {
      (itemCode, reply) =>
        try {
          val result = MdsContext.adapter.collectTest(itemCode, MdsContext.source.items.find(_.item_code == itemCode).get)
          reply(result)
          if (!result) {
            logger.error(s"Collect test error [${result.code}]:${result.message}")
          }
        } catch {
          case e: Throwable =>
            logger.error(s"Collect test error", e)
        }
    })
  }

  protected def fetchCollectTestResp(code: String, callback: (String, Resp[Void] => Unit) => Unit): Unit

  def queryPullResp(code: String): Unit = {
    fetchQueryPullResp(code, {
      (query, reply) =>
        val itemCode = query("itemCode")
        try {
          val result = MdsContext.adapter.queryPull(itemCode, query - "itemCode", MdsContext.source.items.find(_.item_code == itemCode).get)
          reply(result)
          if (!result) {
            logger.error(s"Query pull error [${result.code}]:${result.message}")
          }
        } catch {
          case e: Throwable =>
            logger.error(s"Query pull error", e)
        }
    })
  }

  protected def fetchQueryPullResp(code: String, callback: (Map[String, String], Any => Unit) => Unit): Unit


  def queryPushReq(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = {
        while (true) {
          try {
            val pushMessageR = MdsContext.adapter.queryPush()
            if (!pushMessageR) {
              logger.error(s"Query push error [${pushMessageR.code}]:${pushMessageR.message}")
            } else {
              fetchQueryPushReq(MdsContext.source.code + "_" + pushMessageR.body._1, pushMessageR.body._2)
            }
          } catch {
            case e: Throwable =>
              logger.error(s"Query push error", e)
          }
        }
      }
    }).start()
  }


  protected def fetchQueryPushReq(code: String, message: Any): Unit


}

