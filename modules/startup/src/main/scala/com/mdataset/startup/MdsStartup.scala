package com.mdataset.startup

import com.ecfront.ez.framework.core.EZManager
import com.mdataset.lib.basic.MdsExchangeAPI
import com.mdataset.startup.process.MdsExchangeProcessor
import com.typesafe.scalalogging.slf4j.LazyLogging


object MdsStartup extends App with LazyLogging {

  if (EZManager.start()) {
    MdsExchangeProcessor.processExchange()
  }

}
