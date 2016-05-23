package com.mdataset.service.api.export.site

import java.text.SimpleDateFormat

import com.ecfront.common.{BeanHelper, Resp}
import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.rpc.foundation.{EZRPCContext, GET, RPC}
import com.ecfront.ez.framework.service.rpc.http.HTTP
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.model.{MdsCollectStatusEntity, MdsSourceWithStatusVO}

@RPC("/inter/")
@HTTP
object MdsInterfaceAPI {

  private val dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  @GET("")
  def fetchAllInterfaces(parameter: Map[String, String], context: EZRPCContext): Resp[List[MdsSourceWithStatusVO]] = {
    val vos = MdsContext.sources.flatMap {
      items =>
        val code = items._1
        items._2.values.map {
          item =>
            val vo = new MdsSourceWithStatusVO
            BeanHelper.copyProperties(vo, item)
            val status = MdsCollectStatusEntity.getByCode(code, item.item_code)
            if (status != null) {
              vo.code = code
              vo.status = status.status
              vo.last_update_time = dateF.format(TimeHelper.msf.parse(status.last_update_time))
            }
            vo
        }
    }.toList
    Resp.success(vos)
  }

}
