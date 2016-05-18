package com.mdataset.startup.process

import java.util.Date

import com.ecfront.common.Resp
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.scheduler.{EZ_Scheduler, ScheduleJob, SchedulerProcessor}
import com.mdataset.startup.MdsContext
import com.mdataset.startup.model.MdsCollectStatusEntity

object MdsCollectTestScheduleJob extends ScheduleJob {

  override def execute(scheduler: EZ_Scheduler): Resp[Void] = {
    val code = scheduler.parameters("code").asInstanceOf[String]
    val itemCode = scheduler.parameters("itemCode").asInstanceOf[String]
    MdsContext.defaultExchangeAPI.collectTestReq(code, itemCode, {
      resp =>
        if (!resp) {
          var status = MdsCollectStatusEntity.getByCode(code, itemCode)
          if (status == null) {
            status = new MdsCollectStatusEntity
            status.code = code
            status.item_code = itemCode
          }
          status.last_update_time = TimeHelper.msf.format(new Date())
          status.info = Map[String, Any]()
          status.status = false
          logger.error(s"Collect Test error [${resp.code}]:${resp.message}")
          MdsCollectStatusEntity.saveOrUpdate(status)
        } else {
          Resp.success(null)
        }
    })
    Resp.success(null)
  }

  def add(code: String, itemCode: String, schedule: String): Unit = {
    SchedulerProcessor.delete(code + "_" + itemCode + "_test")
    if (schedule != null && schedule.nonEmpty) {
      val scheduler = EZ_Scheduler()
      scheduler.name = code + "_" + itemCode + "_test"
      scheduler.cron = schedule
      scheduler.module = EZContext.module
      scheduler.clazz = MdsCollectExecScheduleJob.getClass.getName
      scheduler.parameters = Map(
        "code" -> code,
        "itemCode" -> itemCode
      )
      SchedulerProcessor.save(scheduler)
    }
  }

}
