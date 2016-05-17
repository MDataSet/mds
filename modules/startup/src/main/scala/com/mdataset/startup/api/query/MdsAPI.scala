package com.mdataset.startup.api.query

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.rpc.foundation.{EZRPCContext, POST, REQUEST, RPC}
import com.ecfront.ez.framework.service.rpc.http.HTTP
import com.ecfront.ez.framework.service.rpc.websocket.WebSocket
import com.mdataset.lib.basic.MdsExchangeAPI
import com.mdataset.startup.MdsContext
import com.mdataset.startup.process.MdsLimitProcessor

@RPC("/api/")
@HTTP
@WebSocket
object MdsAPI {

  @POST(":code/")
  def pull(parameter: Map[String, String], body: Map[String, String], context: EZRPCContext): Resp[Any] = {
    val code = parameter("code")
    val source = MdsContext.sources(code)
    val limitR = MdsLimitProcessor.limitFilter(source)
    if (limitR) {
      MdsExchangeAPI.Master.queryPull(code, body)
    } else {
      limitR
    }
  }

  @REQUEST(":code/")
  def push(parameter: Map[String, String], body: Map[String, String], context: EZRPCContext): Resp[Any] = {
    Resp.success(null)
  }

}
