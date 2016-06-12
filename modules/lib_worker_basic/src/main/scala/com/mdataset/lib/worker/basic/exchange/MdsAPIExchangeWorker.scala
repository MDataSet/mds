package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceMainDTO}
import com.mdataset.lib.worker.basic.MdsWorkerBasicContext

/**
  * API Service交互接口
  */
trait MdsAPIExchangeWorker extends MdsExchangeWorker {

  /**
    * 注册Worker请求
    *
    * @param source 数据源主体
    */
  def registerReq(source: MdsSourceMainDTO): Unit = {
    logger.info(s"==Register== worker [${MdsWorkerBasicContext.source.code}] request api register.")
    fetchRegisterReq(source)
  }

  /**
    * 注册Worker请求消息实现
    *
    * @param source 数据源主体
    * @return 是否成功
    */
  protected def fetchRegisterReq(source: MdsSourceMainDTO): Resp[Void]

  /**
    * 采集执行响应
    *
    * @param code 数据源code
    */
  def collectExecResp(code: String): Unit = {
    fetchCollectExecResp(code, {
      (status, reply) =>
        logger.info(s"==Collect== worker [${MdsWorkerBasicContext.source.code}] response collect execute.")
        try {
          val result = MdsWorkerBasicContext.adapter.collectExec(
            status.item_code,
            MdsWorkerBasicContext.source.items.find(_.item_code == status.item_code).get,
            status)
          reply(result)
          if (!result) {
            logger.error(s"Collect Exec error [${result.code}]:${result.message}")
          }
        } catch {
          case e: Throwable =>
            logger.error(s"Collect Exec error", e)
        }
    })
  }

  /**
    * 采集执行响应消息实现
    *
    * @param code     数据源code
    * @param callback 收到消息后的处理方法
    */
  protected def fetchCollectExecResp(code: String, callback: (MdsCollectStatusDTO, Resp[MdsCollectStatusDTO] => Unit) => Unit): Unit

  /**
    * 采集测试响应
    *
    * @param code 数据源code
    */
  def collectTestResp(code: String): Unit = {
    fetchCollectTestResp(code, {
      (itemCode, reply) =>
        logger.info(s"==Collect== worker [${MdsWorkerBasicContext.source.code}] response collect test.")
        try {
          val result = MdsWorkerBasicContext.adapter.collectTest(itemCode, MdsWorkerBasicContext.source.items.find(_.item_code == itemCode).get)
          reply(result)
          if (!result) {
            logger.error(s"Collect test error [${result.code}]:${result.message}")
          }
        } catch {
          case e: Throwable =>
            logger.error(s"Collect test error", e)
        }
    })
  }

  /**
    * 采集测试响应消息实现
    *
    * @param code     数据源code
    * @param callback 收到消息后的处理方法
    */
  protected def fetchCollectTestResp(code: String, callback: (String, Resp[Void] => Unit) => Unit): Unit

}

