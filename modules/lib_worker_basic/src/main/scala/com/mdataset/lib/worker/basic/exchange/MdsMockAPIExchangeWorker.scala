package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}

/**
  * API Service交互接口的消息Mock
  *
  */
object MdsMockAPIExchangeWorker extends MdsAPIExchangeWorker {

  override protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void] = {
    Resp.success(null)
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    Resp.success(null)
  }

  override protected def fetchCollectExecResp(code: String, callback: (MdsCollectStatusDTO, (Resp[MdsCollectStatusDTO]) => Unit) => Unit): Unit = {

  }

  override protected def fetchCollectTestResp(code: String, callback: (String, (Resp[Void]) => Unit) => Unit): Unit = {

  }

}

