package com.mdataset.service.bd

import java.util.concurrent.CountDownLatch

import com.ecfront.ez.framework.core.test.BasicSpec
import com.mdataset.lib.basic.model.MdsCollectStatusDTO
import com.mdataset.lib.worker.basic.MdsWorkerBasicContext
import com.mdataset.lib.worker.basic.exchange.MdsMockAPIExchangeWorker
import com.mdataset.worker.test.ServiceAdapter


class TestSpec extends BasicSpec {

  test("Test Test") {
    com.mdataset.lib.worker.basic.MdsWorkerBasicContext.apiExchangeWorker=MdsMockAPIExchangeWorker
    com.mdataset.lib.worker.basic.MdsStartup.init(ServiceAdapter)
    MdsWorkerBasicContext.source.items.foreach {
      item =>
        val status = new MdsCollectStatusDTO
        ServiceAdapter.collectExec(item.item_code, item, status)
    }
    new CountDownLatch(1).await()
  }
}


