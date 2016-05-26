package com.mdataset.service.bd.exchange

import java.util.concurrent.CopyOnWriteArraySet

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsInsertReqDTO, MdsQuerySqlReqDTO, MdsRegisterReqDTO}
import com.mdataset.service.bd.MdsContext
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
  * Worker交互接口
  */
trait MdsDataExchangeMaster extends LazyLogging {

  // 初始化记录，用于限制某些只能调用一次的方法，当值存在时不进入方法
  private val isInit = new CopyOnWriteArraySet[String]()

  /**
    * 注册Worker响应
    */
  def registerResp(): Unit = {
    fetchRegisterResp({
      source =>
        val code = source.code
        logger.info(s"==Register== bd service response worker [$code] register.")
        MdsContext.sources += code -> source
        registerEntityMeta(source)
        insertResp(code)
        queryBySqlResp(code)
        logger.info(s"==Register== worker [$code] successful.")
        Resp.success(null)
    })
  }

  /**
    * 注册Worker响应消息实现
    *
    * @param callback 收到消息后的处理方法
    */
  protected def fetchRegisterResp(callback: MdsRegisterReqDTO => Resp[Void]): Unit

  /**
    * 注册（创建）实体到数据库
    *
    * @param source 注册请求对象
    */
  private def registerEntityMeta(source: MdsRegisterReqDTO): Unit = {
    // TODO create table
  }

  /**
    * 注销Worker响应
    */
  def unRegisterResp(): Unit = {
    fetchUnRegisterResp({
      code =>
        logger.info(s"==UnRegister== bd service response worker [$code] unRegister.")
        MdsContext.sources -= code
        logger.info(s"==UnRegister== worker [$code] successful.")
        Resp.success(null)
    })
  }

  /**
    * 注销Worker响应消息实现
    *
    * @param callback 收到消息后的处理方法
    */
  protected def fetchUnRegisterResp(callback: String => Resp[Void]): Unit

  /**
    * 插入数据响应
    *
    * @param code 数据源code
    */
  def insertResp(code: String): Unit = {
    synchronized {
      if (!isInit.contains("insert_" + code)) {
        fetchInsertResp(code, {
          insertReq =>
            logger.info(s"==Insert== bd service response insert from worker [${insertReq.code}].")
            // TODO save
            Resp.success(null)
        })
        isInit.add("insert_" + code)
      }
    }
  }

  /**
    * 插入数据响应消息实现
    *
    * @param code     数据源code
    * @param callback 收到消息后的处理方法
    */
  protected def fetchInsertResp(code: String, callback: MdsInsertReqDTO => Resp[Void]): Unit

  /**
    * 查询数据响应
    *
    * @param code 数据源code
    */
  def queryBySqlResp(code: String): Unit = {
    synchronized {
      if (!isInit.contains("queryBySqlResp_" + code)) {
        fetchQueryBySqlResp(code, {
          querySqlReq =>
            logger.info(s"==Query== bd service response query from worker [${querySqlReq.code}].")
            // TODO query
            Resp.success(List(
              s"""{"name":"a","age":20,"enable":true}""",
              s"""{"name":"b","age":40,"enable":true}""",
              s"""{"name":"c","age":20,"enable":false}"""
            ))
        })
        isInit.add("queryBySqlResp_" + code)
      }
    }
  }

  /**
    * 查询数据响应消息实现
    *
    * @param code     数据源code
    * @param callback 收到消息后的处理方法
    */
  protected def fetchQueryBySqlResp(code: String, callback: MdsQuerySqlReqDTO => Resp[List[String]]): Unit

}
