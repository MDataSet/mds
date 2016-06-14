package com.mdataset.service.api.exchange

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.model.MdsSourceMainEntity
import com.mdataset.service.api.process.MdsCollectExecScheduleJob
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
  * Worker交互接口
  */
trait MdsAPIExchangeMaster extends LazyLogging {

  /**
    * 注册Worker响应
    */
  def registerResp(): Unit = {
    MdsSourceMainEntity.initCache()
    MdsContext.sources.foreach {
      source =>
        val code = source._1
        source._2.values.foreach {
          item =>
            val itemCode = item.item_code
            MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
            MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
        }
    }
    fetchRegisterResp({
      source =>
        val code = source.code
        logger.info(s"==Register== api service response worker [$code] register.")
        val saveResp = MdsSourceMainEntity.saveOrUpdateWithCache(source)
        if (saveResp) {
          MdsContext.sources(code).values.foreach {
            item =>
              val itemCode = item.item_code
              // 为每个数据项添加调度任务
              MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
              MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
          }
          logger.info(s"==Register== worker [$code] successful.")
        }
        saveResp
    })
  }

  /**
    * 注册Worker响应消息实现
    *
    * @param callback 收到消息后的处理方法
    */
  protected def fetchRegisterResp(callback: MdsSourceMainDTO => Resp[Void]): Unit

  /**
    * 注销Worker响应
    */
  def unRegisterResp(): Unit = {
    fetchUnRegisterResp({
      code =>
        logger.info(s"==UnRegister== api service response worker [$code] unRegister.")
        val resp = MdsSourceMainEntity.removeWithCache(code)
        if (resp) {
          logger.info(s"==UnRegister== worker [$code] successful.")
        }
        resp
    })
  }

  /**
    * 注销Worker响应消息实现
    *
    * @param callback 收到消息后的处理方法
    */
  protected def fetchUnRegisterResp(callback: String => Resp[Void]): Unit

  /**
    * 采集执行请求
    *
    * @param status   当前数据采集状态（最后一次执行的信息）
    * @param callback 收到消息后的处理方法
    */
  def collectExecReq(status: MdsCollectStatusDTO, callback: Resp[MdsCollectStatusDTO] => Unit): Unit = {
    logger.info(s"==Collect== api service request collect execute to worker [${status.code}].")
    fetchCollectExecReq(status, callback)
  }

  /**
    * 采集执行请求消息实现
    *
    * @param status   当前数据采集状态（最后一次执行的信息）
    * @param callback 收到消息后的处理方法
    */
  protected def fetchCollectExecReq(status: MdsCollectStatusDTO, callback: Resp[MdsCollectStatusDTO] => Unit): Unit

  /**
    * 采集测试响应
    *
    * @param code     数据源code
    * @param itemCode 数据项code
    * @param callback 收到消息后的处理方法
    */
  def collectTestReq(code: String, itemCode: String, callback: Resp[Void] => Unit): Unit = {
    logger.info(s"==Collect== api service request collect test to worker [$code}].")
    fetchCollectTestReq(code, itemCode, callback)
  }

  /**
    * 采集测试响应消息实现
    *
    * @param code     数据源code
    * @param itemCode 数据项code
    * @param callback 收到消息后的处理方法
    */
  protected def fetchCollectTestReq(code: String, itemCode: String, callback: Resp[Void] => Unit): Unit

}
