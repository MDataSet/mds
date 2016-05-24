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
        try {
          val result = MdsWorkerBasicContext.adapter.collectExec(
            status.item_code,
            MdsWorkerBasicContext.source.items.find(_.item_code == status.item_code).get,
            status)
          reply(result)
          if (!result) {
            logger.error(s"Collect Exec error [${result.code}]:${result.message}")
          } else {
            queryPushReq(result.body)
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
    * @param callback 成功后的处理方法
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
    * @param callback 成功后的处理方法
    */
  protected def fetchCollectTestResp(code: String, callback: (String, Resp[Void] => Unit) => Unit): Unit

  /**
    * 查询响应
    *
    * @param code 数据源code
    */
  def queryPullResp(code: String): Unit = {
    fetchQueryPullResp(code, {
      (query, reply) =>
        val itemCode = query("itemCode")
        try {
          val result = MdsWorkerBasicContext.adapter.queryPull(
            itemCode,
            query - "itemCode",
            MdsWorkerBasicContext.source.items.find(_.item_code == itemCode).get)
          reply(result)
          if (!result) {
            logger.error(s"Query pull error [${result.code}]:${result.message}")
          }
        } catch {
          case e: Throwable =>
            logger.error(s"Query pull error", e)
        }
    })
  }

  /**
    * 查询响应消息实现
    *
    * @param code     数据源code
    * @param callback 成功后的处理方法
    */
  protected def fetchQueryPullResp(code: String, callback: (Map[String, String], Any => Unit) => Unit): Unit

  /**
    * 推送请求
    *
    * 注意，这是发起方，推送是由Worker发起的（在[[com.mdataset.lib.worker.basic.MdsAdapter.collectExec]]执行成功时触发）
    *
    * @param status 当前数据采集状态（最后一次执行的信息）
    */
  def queryPushReq(status: MdsCollectStatusDTO): Unit = {
    try {
      val pushMessageR = MdsWorkerBasicContext.adapter.queryPush(status)
      if (!pushMessageR) {
        logger.error(s"Query push error [${pushMessageR.code}]:${pushMessageR.message}")
      } else {
        fetchQueryPushReq(MdsWorkerBasicContext.source.code + "_" + pushMessageR.body._1, pushMessageR.body._2)
      }
    } catch {
      case e: Throwable =>
        logger.error(s"Query push error", e)
    }
  }

  /**
    * 推送请求消息实现
    *
    * @param code 数据源code
    * @param data 推送的数据
    */
  protected def fetchQueryPushReq(code: String, data: Any): Unit


}

