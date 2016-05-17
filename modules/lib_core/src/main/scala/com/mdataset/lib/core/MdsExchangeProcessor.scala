package com.mdataset.lib.core

import com.mdataset.lib.basic.MdsExchangeAPI
import com.typesafe.scalalogging.slf4j.LazyLogging

object MdsExchangeProcessor extends LazyLogging {

  def process(): Unit = {
    MdsExchangeAPI.Worker.register(MdsContext.source)
    MdsExchangeAPI.Worker.collectExec(MdsContext.source.code, {
      (collectStatus, reply) =>
        val result = MdsContext.adapter.collectExec(MdsContext.source, collectStatus)
        reply(result)
        if (!result) {
          logger.error(s"Collect Exec error [${result.code}]:${result.message}")
        }
    })
    MdsExchangeAPI.Worker.collectTest(MdsContext.source.code, {
      (_, reply) =>
        val result = MdsContext.adapter.collectTest(MdsContext.source)
        reply(result)
        if (!result) {
          logger.error(s"Collect test error [${result.code}]:${result.message}")
        }
    })
    MdsExchangeAPI.Worker.queryPull(MdsContext.source.code, {
      (query, reply) =>
        val result = MdsContext.adapter.queryPull(query)
        reply(result)
        if (!result) {
          logger.error(s"Query pull error [${result.code}]:${result.message}")
        }
    })
    new Thread(new Runnable {
      override def run(): Unit = {
        while (true) {
          val pushMessageR = MdsContext.adapter.queryPush()
          if (!pushMessageR) {
            logger.error(s"Query push error [${pushMessageR.code}]:${pushMessageR.message}")
          } else {
            MdsExchangeAPI.Worker.queryPush(MdsContext.source.code, pushMessageR.body)
          }
        }
      }
    }).start()
  }
}
