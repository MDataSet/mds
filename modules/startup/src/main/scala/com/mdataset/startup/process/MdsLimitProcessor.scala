package com.mdataset.startup.process

import com.ecfront.common.Resp
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.distributed.DCounterService
import com.mdataset.lib.basic.model.MdsSourceDTO
import io.vertx.core.Handler

object MdsLimitProcessor {

  private val DEFAULT_COUNTER_DEC_PERIODIC: Long = 60000
  private val FLAG_LIMIT = "__mds_limit_"
  private val counters = collection.mutable.Map[String, DCounterService]()

  def addCounter(source: MdsSourceDTO): Unit = {
    counters -= source.code
    if (source.query_limit != null && source.query_limit.hourly_rate != 0) {
      counters += source.code -> DCounterService(FLAG_LIMIT + source.code)
    }
  }

  def limitFilter(source: MdsSourceDTO): Resp[Void] = {
    if (source.query_limit != null && source.query_limit.hourly_rate != 0) {
      if (counters(source.code).inc() >= source.query_limit.hourly_rate) {
        Resp.locked("查询过于频繁,请稍后再试.")
      } else {
        Resp.success(null)
      }
    } else {
      Resp.success(null)
    }
  }

  def init(): Unit = {
    EZContext.vertx.setPeriodic(DEFAULT_COUNTER_DEC_PERIODIC, new Handler[java.lang.Long] {
      override def handle(event: java.lang.Long): Unit = {
        counters.values.foreach {
          counter =>
            counter.dec(0)
        }
      }
    })
  }

  init()

}
