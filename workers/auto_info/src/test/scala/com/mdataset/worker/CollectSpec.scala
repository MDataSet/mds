package com.mdataset.worker

import com.mdataset.lib.core.test.MdsBasicCollectSpec
import com.mdataset.worker.autoinfo.CollectProcessor

class CollectSpec extends MdsBasicCollectSpec {

  test("collect test") {
    CollectProcessor.collect()
  }

}
