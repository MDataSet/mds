package com.mdataset.service.api.exchange

import java.util.concurrent.CopyOnWriteArraySet

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.rpc.foundation.Method
import com.ecfront.ez.framework.service.rpc.websocket.WebSocketMessagePushManager
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsQueryORPushRespDTO, MdsSourceMainDTO}
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.process.{MdsCollectExecScheduleJob, MdsLimitProcessor}
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
  * Worker交互接口
  */
trait MdsAPIExchangeMaster extends LazyLogging {

  // 初始化记录，用于限制某些只能调用一次的方法，当值存在时不进入方法
  private val isInit = new CopyOnWriteArraySet[String]()

  /**
    * 注册Worker响应
    */
  def registerResp(): Unit = {
    fetchRegisterResp({
      source =>
        val code = source.code
        MdsContext.sources += code -> source.items.map(i => i.item_code -> i).toMap
        MdsContext.sources(code).values.foreach {
          item =>
            val itemCode = item.item_code
            // 为每个数据项添加调度任务
            MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
            MdsCollectExecScheduleJob.add(code, itemCode, item.collect_exec_schedule)
            // 添加查询限制
            MdsLimitProcessor.addCounter(code, item)
        }
        pushProcess(code)
        logger.info(s"==Register== [$code] successful.")
        Resp.success(null)
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
        MdsContext.sources -= code
        MdsContext.sources(code).values.foreach {
          item =>
            // 移除WebSocket连接
            WebSocketMessagePushManager.remove(Method.REQUEST, s"/api/$code/${item.item_code}/", matchAll = false)
        }
        logger.info(s"==UnRegister== [$code] successful.")
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
    * 采集执行请求
    *
    * @param status   当前数据采集状态（最后一次执行的信息）
    * @param callback 收到消息后的处理方法
    */
  def collectExecReq(status: MdsCollectStatusDTO, callback: Resp[MdsCollectStatusDTO] => Unit): Unit = {
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

  /**
    * 查询请求
    *
    * @param code     数据源code
    * @param itemCode 数据项code
    * @param clientId 请求id
    * @param query    查询条件
    * @return 查询到的数据
    */
  def queryReq(code: String, itemCode: String, clientId: String, query: Map[String, String]): Unit = {
    val richQuery = query ++ Map("__itemCode__" -> itemCode, "__clientId__" -> clientId)
    fetchQueryReq(code, itemCode, richQuery, {
      resp =>
        if (!resp) {
          logger.error(s"Query error [$code]-[$itemCode]-[$clientId]:$query")
        }
    })
  }

  /**
    * 查询请求消息实现
    *
    * @param code     数据源code
    * @param itemCode 数据项code
    * @param query    查询条件
    * @return 查询到的数据
    */
  protected def fetchQueryReq(code: String, itemCode: String, query: Map[String, String], callback: Resp[Void] => Unit): Unit

  /**
    * 推送处理
    *
    * @param code 数据源code
    */
  def pushProcess(code: String): Unit = {
    synchronized {
      if (!isInit.contains("pushResp_" + code)) {
        fetchPushResp(code, {
          pushResp =>
            // 使用WebSocket推送
            WebSocketMessagePushManager.ws(Method.REQUEST, s"/api/$code/${pushResp.itemCode}/${pushResp.clientId}/", pushResp.data)
            Resp.success(null)
        })
        isInit.contains("pushResp_" + code)
      }
    }
  }

  /**
    * 推送处理消息实现
    *
    * @param code     数据源code
    * @param callback 收到消息后的处理方法
    */
  protected def fetchPushResp(code: String, callback: MdsQueryORPushRespDTO => Resp[Void]): Unit

}
