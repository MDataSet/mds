package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.typesafe.scalalogging.slf4j.LazyLogging


trait MdsExchangeWorker extends LazyLogging {

  def unRegisterReq(code: String): Unit = {
    fetchUnRegisterReq(code)
  }

  protected def fetchUnRegisterReq(code: String): Resp[Void]

}

