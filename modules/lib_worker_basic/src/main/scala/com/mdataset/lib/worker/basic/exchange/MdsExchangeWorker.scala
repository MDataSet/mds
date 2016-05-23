package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}
import com.mdataset.lib.worker.basic.MdsContext
import com.typesafe.scalalogging.slf4j.LazyLogging


trait MdsExchangeWorker extends LazyLogging {

  def registerReq(source: MdsSourceMainDTO): Unit = {
    fetchRegisterReq(source)
  }

  protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void]

  def unRegisterReq(code: String): Unit = {
    fetchUnRegisterReq(code)
  }

  protected def fetchUnRegisterReq(code: String): Resp[Void]

}

