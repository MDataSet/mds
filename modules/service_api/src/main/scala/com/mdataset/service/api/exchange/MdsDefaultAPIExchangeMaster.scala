package com.mdataset.service.api.exchange

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}

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

}
