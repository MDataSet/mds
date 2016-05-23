package com.mdataset.worker

import com.mdataset.lib.worker.basic.test.MdsBasicCollectSpec
import com.mdataset.worker.autoinfo.CollectProcessor

class CollectSpec extends MdsBasicCollectSpec {

  test("collect test") {
    CollectProcessor.collectModel()
  }

}
