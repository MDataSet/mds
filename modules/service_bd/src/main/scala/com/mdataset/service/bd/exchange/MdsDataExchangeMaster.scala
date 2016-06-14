package com.mdataset.service.bd.exchange

import java.util.concurrent.CopyOnWriteArraySet

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsInsertReqDTO, MdsRegisterEntityReqDTO}
import com.mdataset.service.bd.MdsContext
import com.mdataset.service.bd.model.MdsRegisterReqEntity
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
    MdsRegisterReqEntity.initCache()
    MdsContext.sources.foreach {
      source =>
        registerEntityMeta(source._2)
        insertResp(source._1)
    }
    fetchRegisterResp({
      source =>
        val code = source.code
        logger.info(s"==Register== bd service response worker [$code] register.")
        val saveResp = MdsRegisterReqEntity.saveOrUpdateWithCache(source)
        if (saveResp) {
          registerEntityMeta(source)
          insertResp(code)
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
  protected def fetchRegisterResp(callback: MdsRegisterEntityReqDTO => Resp[Void]): Unit

  /**
    * 注册（创建）实体到数据库
    *
    * @param source 注册请求对象
    */
  private def registerEntityMeta(source: MdsRegisterEntityReqDTO): Unit = {
    println(source)
    // TODO create table
  }

  /**
    * 注销Worker响应
    */
  def unRegisterResp(): Unit = {
    fetchUnRegisterResp({
      code =>
        logger.info(s"==UnRegister== bd service response worker [$code] unRegister.")
        val resp = MdsRegisterReqEntity.removeWithCache(code)
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

}
