package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.{MdsInsertReqDTO, MdsRegisterEntityReqDTO}
import com.mdataset.lib.worker.basic.MdsWorkerBasicContext

/**
  * BD Service交互接口的消息默认实现
  *
  * 使用EventBus及Kafka通道
  */
object MdsDefaultDataExchangeWorker extends MdsDataExchangeWorker {

  private lazy val insertProducer = KafkaProcessor.Producer(
    BasicContext.FLAG_DATA_INSERT + MdsWorkerBasicContext.source.code,
    BasicContext.FLAG_DATA_INSERT + MdsWorkerBasicContext.source.code)

  override protected def fetchRegisterReq(source: MdsRegisterEntityReqDTO): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_DATA_REGISTER, source)
    Resp.success(null)
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    EventBusProcessor.send(BasicContext.FLAG_DATA_UN_REGISTER, code)
    Resp.success(null)
  }

  override protected def fetchInsertReq(itemCode: String, data: String): Resp[Void] = {
    insertProducer.send(JsonHelper.toJsonString(MdsInsertReqDTO(MdsWorkerBasicContext.source.code, itemCode, data)))
    Resp.success(null)
  }

}

