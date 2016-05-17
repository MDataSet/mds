package com.mdataset.startup.process

import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.rpc.foundation.Method
import com.ecfront.ez.framework.service.rpc.websocket.WebSocketMessagePushManager
import com.ecfront.ez.framework.service.scheduler.{EZ_Scheduler, SchedulerProcessor}
import com.mdataset.lib.basic.MdsExchangeAPI
import com.mdataset.lib.basic.model.MdsSourceDTO
import com.mdataset.startup.MdsContext

object MdsExchangeProcessor {


  def processExchange(): Unit = {
    MdsExchangeAPI.Master.register(workerRegister)
    MdsExchangeAPI.Master.unRegister(workerUnRegister)
  }

  private def workerRegister: (MdsSourceDTO) => Unit = {
    mdsSourceDTO =>
      MdsContext.sources += mdsSourceDTO.code -> mdsSourceDTO
      SchedulerProcessor.delete(mdsSourceDTO.code + "_exec")
      SchedulerProcessor.delete(mdsSourceDTO.code + "_test")
      if (mdsSourceDTO.collect_exec_schedule != null && mdsSourceDTO.collect_exec_schedule.nonEmpty) {
        val scheduler = EZ_Scheduler()
        scheduler.name = mdsSourceDTO.code + "_exec"
        scheduler.cron = mdsSourceDTO.collect_exec_schedule
        scheduler.module = EZContext.module
        scheduler.clazz = MdsCollectExecScheduleJob.getClass.getName
        scheduler.parameters = Map()
        SchedulerProcessor.save(scheduler)
      }
      if (mdsSourceDTO.collect_test_schedule != null && mdsSourceDTO.collect_test_schedule.nonEmpty) {
        val scheduler = EZ_Scheduler()
        scheduler.name = mdsSourceDTO.code + "_test"
        scheduler.cron = mdsSourceDTO.collect_test_schedule
        scheduler.module = EZContext.module
        scheduler.clazz = MdsCollectTestScheduleJob.getClass.getName
        scheduler.parameters = Map()
        SchedulerProcessor.save(scheduler)
      }

      MdsLimitProcessor.addCounter(mdsSourceDTO)
      MdsExchangeAPI.Master.queryPush(mdsSourceDTO.code, {
        message =>
          WebSocketMessagePushManager.ws(Method.REQUEST, s"/api/${mdsSourceDTO.code}/", message)
      })
  }

  private def workerUnRegister: (String) => Unit = {
    code =>
      MdsContext.sources -= code
      WebSocketMessagePushManager.remove(Method.REQUEST, s"/api/$code/")
  }

}
