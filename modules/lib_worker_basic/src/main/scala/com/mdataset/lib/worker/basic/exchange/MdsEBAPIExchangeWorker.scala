package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}
import com.mdataset.lib.worker.basic.MdsContext

object MdsEBAPIExchangeWorker extends MdsAPIExchangeWorker {

  override protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_API_REGISTER, source)
    Resp.success(null)
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_API_UN_REGISTER, code)
    Resp.success(null)
  }

  override protected def fetchCollectExecResp(code: String, callback: (MdsCollectStatusDTO, (Resp[MdsCollectStatusDTO]) => Unit) => Unit): Unit = {
    MdsContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[MdsCollectStatusDTO](BasicContext.FLAG_API_COLLECT_EXEC + code + "_" + item.item_code, callback)
    }
  }

  override protected def fetchCollectTestResp(code: String, callback: (String, (Resp[Void]) => Unit) => Unit): Unit = {
    MdsContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[String](BasicContext.FLAG_API_COLLECT_TEST + code + "_" + item.item_code, callback)
    }
  }

  override protected def fetchQueryPushReq(code: String, message: Any): Unit = {
    EventBusProcessor.send(BasicContext.FLAG_API_QUERY_PUSH + code, message)
  }

  override protected def fetchQueryPullResp(code: String, callback: (Map[String, String], (Any) => Unit) => Unit): Unit = {
    MdsContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[Map[String, String]](BasicContext.FLAG_API_QUERY_PULL + code + "_" + item.item_code, callback)
    }
  }


}

