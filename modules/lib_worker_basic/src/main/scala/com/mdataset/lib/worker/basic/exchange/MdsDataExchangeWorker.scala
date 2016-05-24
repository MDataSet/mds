package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.fasterxml.jackson.databind.JsonNode
import com.mdataset.lib.basic.model.{MdsIdModel, MdsRegisterReqDTO}

import scala.collection.JavaConversions._

/**
  * BD Service交互接口
  */
trait MdsDataExchangeWorker extends MdsExchangeWorker {

  /**
    * 注册Worker请求
    *
    * @param source BD注册请求对象
    */
  def registerReq(source: MdsRegisterReqDTO): Unit = {
    fetchRegisterReq(source)
  }

  /**
    * 注册Worker请求消息实现
    *
    * @param source BD注册请求对象
    * @return 是否成功
    */
  protected def fetchRegisterReq(source: MdsRegisterReqDTO): Resp[Void]

  /**
    * 插入数据
    *
    * @param lines 数据项
    * @tparam E 数据类型，必须是[[MdsIdModel]]的子类
    * @return 是否成功
    */
  def insert[E <: MdsIdModel](lines: List[E]): Resp[Void] = {
    if (lines != null) {
      val items = lines.map {
        line =>
          JsonHelper.toJsonString(line)
      }
      val tableName = lines.head.getClass.getSimpleName
      fetchInsert(tableName, items)
      Resp.success(null)
    } else {
      Resp.success(null)
    }
  }

  /**
    * 插入数据消息实现
    *
    * @param tableName 表名
    * @param items     数据项
    * @return 是否成功
    */
  protected def fetchInsert(tableName: String, items: List[String]): Resp[Void]

  /**
    * 查询数据
    *
    * @param sql        sql
    * @param parameters 参数
    * @tparam E 数据类型，必须是[[MdsIdModel]]的子类
    * @return 查询到的数据
    */
  def queryBySqlReq[E: Manifest](sql: String, parameters: List[Any]): Resp[List[E]] = {
    val result = fetchQueryBySqlReq(sql, parameters)
    if (result) {
      Resp.success(result.body.map(JsonHelper.toObject[E]).toList)
    } else {
      result
    }
  }

  /**
    * 查询数据消息实现
    *
    * @param sql        sql
    * @param parameters 参数
    * @return 查询到的数据
    */
  protected def fetchQueryBySqlReq(sql: String, parameters: List[Any]): Resp[JsonNode]

}
