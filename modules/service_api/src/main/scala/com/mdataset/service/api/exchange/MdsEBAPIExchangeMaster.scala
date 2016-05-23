package com.mdataset.service.api.exchange

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}

object MdsEBAPIExchangeMaster extends MdsAPIExchangeMaster {

  override protected def fetchRegisterResp(fun: MdsSourceMainDTO => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[MdsSourceMainDTO](BasicContext.FLAG_API_REGISTER, {
      (source, reply) =>
        reply(fun(source))
    })
  }

  override protected def fetchUnRegisterResp(fun: String => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[String](BasicContext.FLAG_API_UN_REGISTER, {
      (code, reply) =>
        reply(fun(code))
    })
  }

  override protected def fetchCollectExecReq(status: MdsCollectStatusDTO, callback: (Resp[MdsCollectStatusDTO]) => Resp[Void]): Unit = {
    EventBusProcessor.Async.sendAdv[Resp[MdsCollectStatusDTO]](BasicContext.FLAG_API_COLLECT_EXEC + status.code + "_" + status.item_code, status, {
      (resp, _) =>
        callback(resp)
    })
  }

  override protected def fetchCollectTestReq(code: String, itemCode: String, callback: (Resp[Void]) => Resp[Void]): Unit = {
    EventBusProcessor.Async.sendAdv[Resp[Void]](BasicContext.FLAG_API_COLLECT_TEST + code + "_" + itemCode, itemCode, {
      (resp, _) =>
        callback(resp)
    })
  }

  override protected def fetchQueryPullReq(code: String, itemCode: String, query: Map[String, String]): Resp[Any] = {
    EventBusProcessor.sendWithReply[Resp[Any]](BasicContext.FLAG_API_QUERY_PULL + code + "_" + itemCode, query + ("itemCode" -> itemCode))._1
  }

  override protected def fetchQueryPushResp(code: String, itemCode: String, callback: Any => Unit): Unit = {
    EventBusProcessor.Async.consumerAdv[Any](BasicContext.FLAG_API_QUERY_PUSH + code + "_" + itemCode, {
      (message, _) =>
        callback(message)
    })
  }
}
