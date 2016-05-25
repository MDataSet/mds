package com.mdataset.service.api.export.query

import java.util.UUID

import com.ecfront.common.Resp
import com.ecfront.ez.framework.service.rpc.foundation._
import com.ecfront.ez.framework.service.rpc.http.HTTP
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.process.MdsLimitProcessor

/**
  * 数据API
  */
@RPC("/api/http/")
@HTTP
object HttpAPI {

  /**
    * 查询入口
    *
    * @param parameter URL参数
    * @param body      查询条件
    * @param context   上下文
    * @return 查询到的数据
    */
  @POST(":code/:itemCode/:clientId/")
  def query(parameter: Map[String, String], body: Map[String, String], context: EZRPCContext): Resp[Any] = {
    val code = parameter("code")
    val itemCode = parameter("itemCode")
    val clientId = parameter("clientId")
    if (MdsContext.sources.contains(code) && MdsContext.sources(code).contains(itemCode)) {
      val item = MdsContext.sources(code)(itemCode)
      val limitR = MdsLimitProcessor.limitFilter(code, item)
      if (limitR) {
        MdsContext.apiExchangeMaster.queryReq(code, itemCode, clientId, body)
        Resp.success(null)
      } else {
        limitR
      }
    } else {
      Resp.notFound("请求资源不存在.")
    }
  }

  @GET("clientId/")
  def getClientId(parameter: Map[String, String], context: EZRPCContext): Resp[String] = {
    Resp.success(UUID.randomUUID().toString)
  }

}
