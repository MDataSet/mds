package com.mdataset.service.api

import com.ecfront.ez.framework.core.EZManager
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
  * API Service启动类
  */
object MdsStartup extends App with LazyLogging {

  if (EZManager.start()) {
    MdsContext.apiExchangeMaster.registerResp()
    MdsContext.apiExchangeMaster.unRegisterResp()
  }

}
