package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.MdsRegisterEntityReqDTO

/**
  * BD Service交互接口的消息Mock
  *
  */
object MdsMockDataExchangeWorker extends MdsDataExchangeWorker {


  override protected def fetchRegisterReq(source: MdsRegisterEntityReqDTO): Resp[Void] = {
    Resp.success(null)
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    Resp.success(null)
  }

  override protected def fetchInsertReq(itemCode: String, data: String): Resp[Void] = {
    Resp.success(null)
  }

}

