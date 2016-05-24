package com.mdataset.service.api.process

import java.util.Date

import com.ecfront.common.Resp
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.scheduler.{EZ_Scheduler, ScheduleJob, SchedulerProcessor}
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.model.MdsCollectStatusEntity

/**
  * 采集测试调度器回调类
  */
object MdsCollectTestScheduleJob extends ScheduleJob {

  override def execute(scheduler: EZ_Scheduler): Resp[Void] = {
    val code = scheduler.parameters("code").asInstanceOf[String]
    val itemCode = scheduler.parameters("itemCode").asInstanceOf[String]
    // 发起采集测试请求
    MdsContext.apiExchangeMaster.collectTestReq(code, itemCode, {
      resp =>
        if (!resp) {
          var status = MdsCollectStatusEntity.getByCode(code, itemCode)
          if (status == null) {
            status = new MdsCollectStatusEntity
            status.code = code
            status.item_code = itemCode
            status.info = Map[String, Any]()
          }
          // 采集测试完成后（无论成功还是失败）持久化最新状态
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
