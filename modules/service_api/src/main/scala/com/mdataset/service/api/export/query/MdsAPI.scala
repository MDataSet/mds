package com.mdataset.service.api.export.query

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.rpc.foundation._
import com.ecfront.ez.framework.service.rpc.http.HTTP
import com.ecfront.ez.framework.service.rpc.websocket.{WebSocket, WebSocketMessagePushManager}
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.process.MdsLimitProcessor

/**
  * 数据API
  */
@RPC("/api/")
@HTTP
@WebSocket
object MdsAPI {

  /**
    * 查询入口
    *
    * @param parameter URL参数
    * @param body      查询条件
    * @param context   上下文
    * @return 查询到的数据
    */
  @POST(":code/:itemCode/")
  def pull(parameter: Map[String, String], body: Map[String, String], context: EZRPCContext): Resp[Any] = {
    val code = parameter("code")
    val itemCode = parameter("itemCode")
    if (MdsContext.sources.contains(code) && MdsContext.sources(code).contains(itemCode)) {
      val item = MdsContext.sources(code)(itemCode)
      val limitR = MdsLimitProcessor.limitFilter(code, item)
      if (limitR) {
        MdsContext.apiExchangeMaster.queryPullReq(code, itemCode, body)
      } else {
        limitR
      }
    } else {
      Resp.notFound("请求资源不存在.")
    }
  }

  /**
    * 推送激活入口
    *
    * @param parameter URL参数
    * @param body      推送条件
    * @param context   上下文
    * @return 推送的数据
    */
  @REQUEST(":code/:itemCode/")
  def push(parameter: Map[String, String], body: Map[String, String], context: EZRPCContext): Resp[Any] = {
    val code = parameter("code")
    val itemCode = parameter("itemCode")
    if (MdsContext.sources.contains(code) && MdsContext.sources(code).contains(itemCode)) {
      Resp.success(null)
    } else {
      WebSocketMessagePushManager.remove(Method.REQUEST, s"/api/$code/$itemCode/")
      Resp.notFound("请求资源不存在.")
    }
  }

}
