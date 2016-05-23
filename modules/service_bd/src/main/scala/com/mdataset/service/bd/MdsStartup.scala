package com.mdataset.service.bd

import com.ecfront.ez.framework.core.EZManager
import com.mdataset.service.api.MdsContext
import com.typesafe.scalalogging.slf4j.LazyLogging


object MdsStartup extends App with LazyLogging {

  if (EZManager.start()) {
    MdsContext.defaultDataExchangeMaster.registerResp()
    MdsContext.defaultDataExchangeMaster.unRegisterResp()
  }

}
