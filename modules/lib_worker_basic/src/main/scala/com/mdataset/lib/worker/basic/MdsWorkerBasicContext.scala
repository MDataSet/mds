package com.mdataset.lib.worker.basic

import com.mdataset.lib.basic.model.MdsSourceMainDTO
import com.mdataset.lib.worker.basic.exchange.{MdsDefaultAPIExchangeWorker, MdsDefaultDataExchangeWorker}

/**
  * Worker基础上下文
  */
object MdsWorkerBasicContext {

  // Worker服务适配器
  var adapter: MdsAdapter = _
  // 数据源主体
  var source: MdsSourceMainDTO = _

  // API Service交互实现
  lazy val apiExchangeWorker = MdsDefaultAPIExchangeWorker
  // BD Service交互实现
  lazy val dataExchangeWorker = MdsDefaultDataExchangeWorker

}
