package com.mdataset.service.api

import com.ecfront.ez.framework.core.EZManager
import com.mdataset.service.api.export.query.SocketAPI
import com.mdataset.service.api.process.QueryProcessor
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
  * API Service启动类
  */
object MdsStartup extends App with LazyLogging {

  if (EZManager.start()) {
    MdsContext.apiExchangeMaster.registerResp()
    MdsContext.apiExchangeMaster.unRegisterResp()
    SocketAPI.listening(MdsContext.socketPort, MdsContext.socketHost)
    logger.info("==Startup== api service startup.")
  }

}
