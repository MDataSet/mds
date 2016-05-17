package com.mdataset.startup.process

import com.ecfront.common.Resp
import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.scheduler.{EZ_Scheduler, ScheduleJob}
import com.mdataset.lib.basic.MdsExchangeAPI
import com.mdataset.startup.model.MdsCollectStatusEntity
import com.mdataset.startup.model.MdsCollectStatusEntity._

object MdsCollectExecScheduleJob extends ScheduleJob {

  override def execute(scheduler: EZ_Scheduler): Resp[Void] = {
    val sourceCode = scheduler.name
    var status = MdsCollectStatusEntity.getByCode(sourceCode)
    MdsExchangeAPI.Master.collectExec(sourceCode, status, {
      resp =>
        if (status == null) {
          status = new MdsCollectStatusEntity
          status.code = sourceCode
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

}
