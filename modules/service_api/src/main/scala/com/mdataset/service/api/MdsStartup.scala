package com.mdataset.service.api

import com.ecfront.ez.framework.core.EZManager
import com.typesafe.scalalogging.slf4j.LazyLogging


object MdsStartup extends App with LazyLogging {

  if (EZManager.start()) {
    MdsContext.defaultExchangeAPI.registerResp()
    MdsContext.defaultExchangeAPI.unRegisterResp()
  }

}
