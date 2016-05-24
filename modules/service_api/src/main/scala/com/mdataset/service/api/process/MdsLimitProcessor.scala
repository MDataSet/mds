package com.mdataset.service.api.process

import com.ecfront.common.Resp
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.distributed.DCounterService
import com.mdataset.lib.basic.model.MdsSourceItemDTO
import io.vertx.core.Handler

object MdsLimitProcessor {

  private val DEFAULT_COUNTER_DEC_PERIODIC: Long = 60000
  private val FLAG_LIMIT = "__mds_limit_"
  private val counters = collection.mutable.Map[String, DCounterService]()

  def addCounter(code: String, item: MdsSourceItemDTO): Unit = {
    counters -= code + "_" + item.item_code
    if (item.query_limit != null && item.query_limit.hourly_max_times != 0) {
      counters += code + "_" + item.item_code -> DCounterService(FLAG_LIMIT + code + "_" + item.item_code)
    }
  }

  def limitFilter(code: String, item: MdsSourceItemDTO): Resp[Void] = {
    if (item.query_limit != null && item.query_limit.hourly_max_times != 0) {
      if (counters(code + "_" + item.item_code).inc() >= item.query_limit.hourly_max_times) {
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
