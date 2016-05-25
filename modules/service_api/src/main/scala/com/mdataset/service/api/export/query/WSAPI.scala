package com.mdataset.service.api.export.query

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.rpc.foundation._
import com.ecfront.ez.framework.service.rpc.websocket.{WebSocket, WebSocketMessagePushManager}
import com.mdataset.service.api.MdsContext

/**
  * 数据API
  */
@RPC("/api/ws/")
@WebSocket
object WSAPI {

  /**
    * 推送激活入口
    *
    * @param parameter URL参数
    * @param body      推送条件
    * @param context   上下文
    * @return 推送的数据
    */
  @REQUEST(":code/:itemCode/:clientId/")
  def push(parameter: Map[String, String], body: Map[String, String], context: EZRPCContext): Resp[Any] = {
    val code = parameter("code")
    val itemCode = parameter("itemCode")
    val clientId = parameter("clientId")
    if (MdsContext.sources.contains(code) && MdsContext.sources(code).contains(itemCode)) {
      Resp.success(null)
    } else {
      WebSocketMessagePushManager.remove(Method.REQUEST, s"/api/$code/$itemCode/$clientId/")
      Resp.notFound("请求资源不存在.")
    }
  }

}
