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

  /**
    * 查询响应
    *
    * @param code 数据源code
    */
  def queryResp(code: String): Unit = {
    fetchQueryResp(code, {
      (query, reply) =>
        val itemCode = query("__itemCode__")
        val clientId = query("__clientId__")
        try {
          val result = MdsWorkerBasicContext.adapter.query(
            itemCode,
            query - "__itemCode__" - "__clientId__",
            MdsWorkerBasicContext.source.items.find(_.item_code == itemCode).get)
          if (result) {
            MdsWorkerBasicContext.dataExchangeWorker.queryBySqlReq(itemCode, result.body._1, result.body._2, clientId)
          } else {
            logger.error(s"Query pull error [${result.code}]:${result.message}")
          }
          reply(result)
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
    * @param callback 收到消息后的处理方法
    */
  protected def fetchQueryResp(code: String, callback: (Map[String, String], Resp[Void] => Unit) => Unit): Unit

}

