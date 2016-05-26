package com.mdataset.service.api

import com.ecfront.ez.framework.core.EZManager
import com.ecfront.ez.framework.core.test.BasicSpec
import com.mdataset.service.api.export.query.SocketAPI

class MockStartupSpec extends BasicSpec {

  before {
    EZManager.start()
    MdsContext.apiExchangeMaster.registerResp()
    MdsContext.apiExchangeMaster.unRegisterResp()
    SocketAPI.listening(MdsContext.socketPort, MdsContext.socketHost)
  }

}
