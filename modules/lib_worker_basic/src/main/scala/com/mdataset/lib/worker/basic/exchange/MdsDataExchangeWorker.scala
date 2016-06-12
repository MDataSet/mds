package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.mdataset.lib.basic.model.{MdsBaseEntity, MdsRegisterEntityReqDTO}
import com.mdataset.lib.worker.basic.MdsWorkerBasicContext

/**
  * BD Service交互接口
  */
trait MdsDataExchangeWorker extends MdsExchangeWorker {

  /**
    * 注册Worker请求
    *
    * @param source BD注册请求对象
    */
  def registerReq(source: MdsRegisterEntityReqDTO): Unit = {
    logger.info(s"==Register== worker [${MdsWorkerBasicContext.source.code}] request bd register.")
    fetchRegisterReq(source)
  }

  /**
    * 注册Worker请求消息实现
    *
    * @param source BD注册请求对象
    * @return 是否成功
    */
  protected def fetchRegisterReq(source: MdsRegisterEntityReqDTO): Resp[Void]

  /**
    * 插入数据请求
    *
    * @param itemCode 数据项code
    * @param data     数据
    * @tparam E 数据类型，必须是[[MdsBaseEntity]]的子类
    * @return 是否成功
    */
  def insertReq[E <: MdsBaseEntity](itemCode: String, data: E): Resp[Void] = {
    if (data != null) {
      logger.info(s"==Insert== worker [${MdsWorkerBasicContext.source.code}] request bd register.")
      fetchInsertReq(itemCode, JsonHelper.toJsonString(data))
    }
    Resp.success(null)
  }

  /**
    * 插入数据请求消息实现
    *
    * @param itemCode 数据项code
    * @param data     数据
    * @return 是否成功
    */
  protected def fetchInsertReq(itemCode: String, data: String): Resp[Void]

  /**
    * 查询数据请求
    *
    * @param itemCode   数据项code
    * @param sql        sql
    * @param parameters 参数
    * @param clientId   请求id
    * @return 是否成功
    */
  def queryBySqlReq(itemCode: String, sql: String, parameters: List[Any], clientId: String = ""): Unit = {
    logger.info(s"==Query== worker [${MdsWorkerBasicContext.source.code}] request query to bd service by client [$clientId].")
    fetchQueryBySqlReq(itemCode, clientId, sql, parameters)
  }

  /**
    * 查询数据请求消息实现
    *
    * @param itemCode   数据项code
    * @param clientId   请求id
    * @param sql        sql
    * @param parameters 参数
    * @return 查询到的数据
    */
  protected def fetchQueryBySqlReq(itemCode: String, clientId: String, sql: String, parameters: List[Any]): Resp[Void]

}
