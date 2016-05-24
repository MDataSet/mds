package com.mdataset.service.bd

import com.ecfront.ez.framework.core.test.BasicSpec
import com.mdataset.worker.mock.{Model, ServiceAdapter}


class MockSpec extends BasicSpec {

  test("Mock Test") {
    com.mdataset.lib.worker.basic.MdsStartup.init(ServiceAdapter)
    com.mdataset.lib.worker.basic.MdsContext.dataExchangeWorker.insert(List(
      Model("a", 20, enable = true),
      Model("b", 40, enable = true),
      Model("c", 20, enable = false)
    ))
    val result = com.mdataset.lib.worker.basic.MdsContext.dataExchangeWorker.queryBySqlReq[Model]("SELECT * FROM model WHERE age > ?", List(20))
    println(result)
  }
}


