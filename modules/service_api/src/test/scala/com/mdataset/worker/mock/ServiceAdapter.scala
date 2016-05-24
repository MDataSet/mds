package com.mdataset.worker.mock

import java.util.Date

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceItemDTO}
import com.mdataset.lib.worker.basic.MdsAdapter

object ServiceAdapter extends MdsAdapter {

  override def collectExec(itemCode: String, source: MdsSourceItemDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO] = {
    status.last_update_time = new Date()
    Resp.success(status)
  }

  override def collectTest(itemCode: String, source: MdsSourceItemDTO): Resp[Void] = {
    Resp.success(null)
  }

  override def queryPush(status: MdsCollectStatusDTO): Resp[(String, Any)] = {
    Resp.success(("model",s"""{"t":1}"""))
  }

  override def queryPull(itemCode: String, query: Map[String, String], source: MdsSourceItemDTO): Resp[Any] = {
    Resp.success(s"""{"t":1}""")
  }

}
