package com.mdataset.service.api.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsQueryORPushRespDTO, MdsSourceMainDTO, QueryReqDTO}

/**
  * Worker交互接口的消息默认实现
  *
  * 使用EventBus通道
  */
object MdsDefaultAPIExchangeMaster extends MdsAPIExchangeMaster {

  override protected def fetchRegisterResp(callback: MdsSourceMainDTO => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[MdsSourceMainDTO](BasicContext.FLAG_API_REGISTER, {
      (source, reply) =>
        reply(callback(source))
    })
  }

  override protected def fetchUnRegisterResp(callback: String => Resp[Void]): Unit = {
    EventBusProcessor.Async.consumerAdv[String](BasicContext.FLAG_API_UN_REGISTER, {
      (code, reply) =>
        reply(callback(code))
    })
  }

  override protected def fetchCollectExecReq(status: MdsCollectStatusDTO, callback: Resp[MdsCollectStatusDTO] => Unit): Unit = {
    EventBusProcessor.Async.sendAdv[Resp[MdsCollectStatusDTO]](BasicContext.FLAG_API_COLLECT_EXEC + status.code + "_" + status.item_code, status, {
      (resp, _) =>
        callback(resp)
    })
  }

  override protected def fetchCollectTestReq(code: String, itemCode: String, callback: Resp[Void] => Unit): Unit = {
    EventBusProcessor.Async.sendAdv[Resp[Void]](BasicContext.FLAG_API_COLLECT_TEST + code + "_" + itemCode, itemCode, {
      (resp, _) =>
        callback(resp)
    })
  }

  override protected def fetchQueryReq(req: QueryReqDTO, callback: Resp[Void] => Unit): Unit = {
    EventBusProcessor.Async.sendAdv[Resp[Void]](BasicContext.FLAG_API_QUERY + req.sourceCode + "_" + req.sourceItemCode, req, {
      (resp, _) =>
        callback(resp)
    })
  }

  override protected def fetchPushResp(code: String, callback: MdsQueryORPushRespDTO => Resp[Void]): Unit = {
    KafkaProcessor.Consumer(BasicContext.FLAG_API_QUERY_OR_PUSH_RESP + code, EZContext.module).receive({
      (message, _) =>
        val pushResp = JsonHelper.toObject[MdsQueryORPushRespDTO](message)
        callback(pushResp)
    })
  }

}
