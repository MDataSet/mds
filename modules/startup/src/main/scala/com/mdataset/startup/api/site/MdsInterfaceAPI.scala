package com.mdataset.startup.api.site

import java.text.SimpleDateFormat

import com.ecfront.common.{BeanHelper, Resp}
import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.rpc.foundation.{EZRPCContext, GET, RPC}
import com.ecfront.ez.framework.service.rpc.http.HTTP
import com.mdataset.startup.MdsContext
import com.mdataset.startup.model.{MdsCollectStatusEntity, MdsSourceWithStatusVO}

@RPC("/inter/")
@HTTP
object MdsInterfaceAPI {

  private val dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  @GET("")
  def fetchAllInterfaces(parameter: Map[String, String], context: EZRPCContext): Resp[List[MdsSourceWithStatusVO]] = {
    val vos=MdsContext.sources.values.map {
      source =>
        val vo = new MdsSourceWithStatusVO
        BeanHelper.copyProperties(vo, source)
        val status = MdsCollectStatusEntity.getByCode(source.code)
        if (status != null) {
          vo.status = status.status
          vo.last_update_time = dateF.format(TimeHelper.msf.parse(status.last_update_time))
        }
        vo
    }.toList
    Resp.success(vos)
  }

}
