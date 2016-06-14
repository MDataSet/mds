package com.mdataset.worker

import com.mdataset.lib.worker.basic.test.MdsMockStartupSpec
import com.mdataset.worker.autoinfo.CollectProcessor

class CollectSpec extends MdsMockStartupSpec {

  test("collect test") {
    CollectProcessor.collectModel()
  }

}
