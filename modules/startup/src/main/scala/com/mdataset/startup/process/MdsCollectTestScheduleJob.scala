package com.mdataset.startup.process

import java.util.Date

import com.ecfront.common.Resp
import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.scheduler.{EZ_Scheduler, ScheduleJob}
import com.mdataset.lib.basic.MdsExchangeAPI
import com.mdataset.startup.model.MdsCollectStatusEntity

object MdsCollectTestScheduleJob extends ScheduleJob {

  override def execute(scheduler: EZ_Scheduler): Resp[Void] = {
    MdsExchangeAPI.Master.collectTest(scheduler.name, {
      resp =>
        if (!resp) {
          val sourceCode = scheduler.name
          var status = MdsCollectStatusEntity.getByCode(sourceCode)
          if (status == null) {
            status = new MdsCollectStatusEntity
            status.code = sourceCode
          }
          status.last_update_time = TimeHelper.msf.format(new Date())
          status.info = Map[String, Any]()
          status.status = false
          MdsCollectStatusEntity.saveOrUpdate(status)
          logger.error(s"Collect Test error [${resp.code}]:${resp.message}")
        }
    })
    Resp.success(null)
  }

}
