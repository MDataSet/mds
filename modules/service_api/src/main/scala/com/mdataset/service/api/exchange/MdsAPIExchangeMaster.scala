package com.mdataset.service.api.exchange

import java.util.concurrent.CopyOnWriteArraySet

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.rpc.foundation.Method
import com.ecfront.ez.framework.service.rpc.websocket.WebSocketMessagePushManager
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.process.{MdsCollectExecScheduleJob, MdsLimitProcessor}
import com.typesafe.scalalogging.slf4j.LazyLogging

trait MdsAPIExchangeMaster extends LazyLogging {

  private val isInit = new CopyOnWriteArraySet[String]()

  def registerResp(): Unit = {
    fetchRegisterResp({
      source =>
        val code = source.code
        MdsContext.sources += code -> source.items.map(i => i.item_code -> i).toMap
        MdsContext.sources(code).values.foreach {
          item =>
            val itemCode = item.item_code
            MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
            MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
            MdsLimitProcessor.addCounter(code, item)
            queryPushResp(code, itemCode)
        }
        Resp.success(null)
    })
  }

  protected def fetchRegisterResp(fun: MdsSourceMainDTO => Resp[Void]): Unit

  def unRegisterResp(): Unit = {
    fetchUnRegisterResp({
      code =>
        MdsContext.sources -= code
        MdsContext.sources(code).values.foreach {
          item =>
            WebSocketMessagePushManager.remove(Method.REQUEST, s"/api/$code/${item.item_code}/")
        }
        Resp.success(null)
    })
  }

  protected def fetchUnRegisterResp(fun: String => Resp[Void]): Unit

  def collectExecReq(status: MdsCollectStatusDTO, callback: Resp[MdsCollectStatusDTO] => Resp[Void]): Unit = {
    fetchCollectExecReq(status, callback)
  }

  protected def fetchCollectExecReq(status: MdsCollectStatusDTO, callback: Resp[MdsCollectStatusDTO] => Resp[Void]): Unit

  def collectTestReq(code: String, itemCode: String, callback: Resp[Void] => Resp[Void]): Unit = {
    fetchCollectTestReq(code, itemCode, callback)
  }

  protected def fetchCollectTestReq(code: String, itemCode: String, callback: Resp[Void] => Resp[Void]): Unit

  def queryPushResp(code: String, itemCode: String): Unit = {
    synchronized {
      if (!isInit.contains("queryPushResp_" + code)) {
        fetchQueryPushResp(code, itemCode, {
          message =>
            WebSocketMessagePushManager.ws(Method.REQUEST, s"/api/$code/$itemCode/", message)
        })
        isInit.contains("queryPushResp_" + code)
      }
    }
  }

  protected def fetchQueryPushResp(code: String, itemCode: String, callback: Any => Unit): Unit

  def queryPullReq(code: String, itemCode: String, query: Map[String, String]): Resp[Any] = {
    fetchQueryPullReq(code, itemCode, query)
  }

  protected def fetchQueryPullReq(code: String, itemCode: String, query: Map[String, String]): Resp[Any]

}
