package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO, QueryReqDTO}
import com.mdataset.lib.worker.basic.MdsWorkerBasicContext

/**
  * API Service交互接口的消息默认实现
  *
  * 使用EventBus通道
  */
object MdsDefaultAPIExchangeWorker extends MdsAPIExchangeWorker {

  override protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void] = {
    EventBusProcessor.sendWithReply[Resp[Void]](BasicContext.FLAG_API_REGISTER, source)._1
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    EventBusProcessor.sendWithReply[Resp[Void]](BasicContext.FLAG_API_UN_REGISTER, code)._1
  }

  override protected def fetchCollectExecResp(code: String, callback: (MdsCollectStatusDTO, (Resp[MdsCollectStatusDTO]) => Unit) => Unit): Unit = {
    MdsWorkerBasicContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[MdsCollectStatusDTO](BasicContext.FLAG_API_COLLECT_EXEC + code + "_" + item.item_code, callback)
    }
  }

  override protected def fetchCollectTestResp(code: String, callback: (String, (Resp[Void]) => Unit) => Unit): Unit = {
    MdsWorkerBasicContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[String](BasicContext.FLAG_API_COLLECT_TEST + code + "_" + item.item_code, callback)
    }
  }

}

