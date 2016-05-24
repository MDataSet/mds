package com.mdataset.service.api.process

import com.ecfront.common.Resp
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.scheduler.{EZ_Scheduler, ScheduleJob, SchedulerProcessor}
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.model.MdsCollectStatusEntity

object MdsCollectExecScheduleJob extends ScheduleJob {

  override def execute(scheduler: EZ_Scheduler): Resp[Void] = {
    val code = scheduler.parameters("code").asInstanceOf[String]
    val itemCode = scheduler.parameters("itemCode").asInstanceOf[String]
    var status = MdsCollectStatusEntity.getByCode(code, itemCode)
    MdsContext.apiExchangeMaster.collectExecReq(status, {
      resp =>
        if (status == null) {
          status = new MdsCollectStatusEntity
          status.code = code
          status.item_code = itemCode
        }
        if (resp) {
          status.last_update_time = TimeHelper.msf.format(resp.body.last_update_time)
          status.info = resp.body.info
          status.status = true
        } else {
          status.status = false
          logger.error(s"Collect Exec error [${resp.code}]:${resp.message}")
        }
        MdsCollectStatusEntity.saveOrUpdate(status)
    })
    Resp.success(null)
  }

  def add(code: String, itemCode: String, schedule: String): Unit = {
    SchedulerProcessor.delete(code + "_" + itemCode + "_exec")
    if (schedule != null && schedule.nonEmpty) {
      val scheduler = EZ_Scheduler()
      scheduler.name = code + "_" + itemCode + "_exec"
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
