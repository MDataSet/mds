package com.mdataset.lib.worker.basic.test

import com.ecfront.ez.framework.core.test.BasicSpec
import com.mdataset.lib.worker.basic.exchange.{MdsMockAPIExchangeWorker, MdsMockDataExchangeWorker}
import com.mdataset.lib.worker.basic.{MdsStartup, MdsWorkerBasicContext}

/**
  * 测试基类
  */
abstract class MdsMockStartupSpec extends BasicSpec {

  before {
    MdsWorkerBasicContext.apiExchangeWorker = MdsMockAPIExchangeWorker
    MdsWorkerBasicContext.dataExchangeWorker = MdsMockDataExchangeWorker
    MdsStartup.init()
  }

}
