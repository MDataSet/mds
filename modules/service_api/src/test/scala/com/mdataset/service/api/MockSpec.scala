package com.mdataset.service.api

import java.util.concurrent.CountDownLatch

import com.ecfront.ez.framework.core.test.BasicSpec
import com.mdataset.worker.mock.ServiceAdapter


class MockSpec extends BasicSpec {

  test("Mock Test") {
    com.mdataset.lib.worker.basic.MdsStartup.init(ServiceAdapter)
    new CountDownLatch(1).await()
  }
}


