package com.mdataset.lib.basic

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.eventbus.EventBusProcessor
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceDTO}

object MdsExchangeAPI {

  private val FLAG_REGISTER: String = "mds_register"
  private val FLAG_UN_REGISTER: String = "mds_unRegister"
  private val FLAG_COLLECT_EXEC: String = "mds_collectExec_"
  private val FLAG_COLLECT_TEST: String = "mds_collectTest_"
  private val FLAG_QUERY_PUSH: String = "mds_queryPush_"
  private val FLAG_QUERY_PULL: String = "mds_queryPull_"

  object Worker {

    def register(source: MdsSourceDTO): Unit = {
      EventBusProcessor.send(FLAG_REGISTER, source)
    }

    def unRegister(code: String): Unit = {
      EventBusProcessor.send(FLAG_UN_REGISTER, code)
    }

    def collectExec(code: String, callback: (MdsCollectStatusDTO, Resp[MdsCollectStatusDTO] => Unit) => Unit): Unit = {
      EventBusProcessor.Async.consumerAdv[MdsCollectStatusDTO](FLAG_COLLECT_EXEC + code, callback)
    }

    def collectTest(code: String, callback: (Void, Resp[Void] => Unit) => Unit): Unit = {
      EventBusProcessor.Async.consumerAdv[Void](FLAG_COLLECT_TEST + code, callback)
    }

    def queryPush(code: String, message: Any): Unit = {
      EventBusProcessor.send(FLAG_QUERY_PUSH + code, message)
    }

    def queryPull(code: String, callback: (Map[String, String], Any => Unit) => Unit): Unit = {
      EventBusProcessor.Async.consumerAdv[Map[String, String]](FLAG_QUERY_PULL + code, callback)
    }

  }

  object Master {

    def register(fun: MdsSourceDTO => Unit): Unit = {
      EventBusProcessor.Async.consumerAdv[MdsSourceDTO](FLAG_REGISTER, {
        (source, _) =>
          fun(source)
      })
    }

    def unRegister(fun: String => Unit): Unit = {
      EventBusProcessor.Async.consumerAdv[String](FLAG_UN_REGISTER, {
        (code, _) =>
          fun(code)
      })
    }

    def collectExec(code: String, status: MdsCollectStatusDTO, callback: Resp[MdsCollectStatusDTO] => Unit): Unit = {
      EventBusProcessor.Async.sendAdv[Resp[MdsCollectStatusDTO]](FLAG_COLLECT_EXEC + code, status, {
        (resp, _) =>
          callback(resp)
      })
    }

    def collectTest(code: String, callback: Resp[Void] => Unit): Unit = {
      EventBusProcessor.Async.sendAdv[Resp[Void]](FLAG_COLLECT_TEST + code, null, {
        (resp, _) =>
          callback(resp)
      })
    }

    def queryPush(code: String, callback: Any => Unit): Unit = {
      EventBusProcessor.Async.consumerAdv[Any](FLAG_QUERY_PUSH + code, {
        (message, _) =>
          callback(message)
      })
    }

    def queryPull(code: String, query: Map[String, String]): Resp[Any] = {
      EventBusProcessor.sendWithReply[Resp[Any]](FLAG_QUERY_PULL + code, query)._1
    }

  }

}
