package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}
import com.mdataset.lib.worker.basic.MdsContext

object MdsEBExchangeWorkerAPI extends MdsExchangeWorkerAPI {

  private val FLAG_REGISTER: String = "mds_register"
  private val FLAG_UN_REGISTER: String = "mds_unRegister"
  private val FLAG_COLLECT_EXEC: String = "mds_collectExec_"
  private val FLAG_COLLECT_TEST: String = "mds_collectTest_"
  private val FLAG_QUERY_PUSH: String = "mds_queryPush_"
  private val FLAG_QUERY_PULL: String = "mds_queryPull_"

  override protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void] = {
    EventBusProcessor.send(FLAG_REGISTER, source)
    Resp.success(null)
  }

  override protected def fetchUnRegisterReq(code: String): Resp[Void] = {
    EventBusProcessor.send(FLAG_UN_REGISTER, code)
    Resp.success(null)
  }

  override protected def fetchCollectExecResp(code: String, callback: (MdsCollectStatusDTO, (Resp[MdsCollectStatusDTO]) => Unit) => Unit): Unit = {
    MdsContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[MdsCollectStatusDTO](FLAG_COLLECT_EXEC + code + "_" + item.item_code, callback)
    }
  }

  override protected def fetchCollectTestResp(code: String, callback: (String, (Resp[Void]) => Unit) => Unit): Unit = {
    MdsContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[String](FLAG_COLLECT_TEST + code + "_" + item.item_code, callback)
    }
  }

  override protected def fetchQueryPushReq(code: String, message: Any): Unit = {
    EventBusProcessor.send(FLAG_QUERY_PUSH + code, message)
  }

  override protected def fetchQueryPullResp(code: String, callback: (Map[String, String], (Any) => Unit) => Unit): Unit = {
    MdsContext.source.items.foreach {
      item =>
        EventBusProcessor.Async.consumerAdv[Map[String, String]](FLAG_QUERY_PULL + code + "_" + item.item_code, callback)
    }
  }


}

