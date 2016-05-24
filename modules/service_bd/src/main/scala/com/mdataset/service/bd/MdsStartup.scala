package com.mdataset.service.bd

import com.ecfront.ez.framework.core.EZManager
import com.typesafe.scalalogging.slf4j.LazyLogging


object MdsStartup extends App with LazyLogging {

  if (EZManager.start()) {
    MdsContext.dataExchangeMaster.registerResp()
    MdsContext.dataExchangeMaster.unRegisterResp()
  }

}
