package com.mdataset.worker.autoinfo

import java.util.Date

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceItemDTO}
import com.mdataset.lib.worker.basic.{MdsAdapter, MdsContext}

object ServiceAdapter extends MdsAdapter {

  override def collectExec(itemCode: String, source: MdsSourceItemDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO] = {
    status.last_update_time = new Date()
    val resp = itemCode match {
      case CollectProcessor.ITEM_CODE_MODEL => CollectProcessor.collectModel()
    }
    if (resp) {
      Resp.success(status)
    } else {
      resp
    }
  }

  override def collectTest(itemCode: String, source: MdsSourceItemDTO): Resp[Void] = {
    Resp.success(null)
  }

  override def queryPush(): Resp[(String, Any)] = {
    Thread.sleep(10000)
    Resp.success(("model",s"""{"t":1}"""))
  }

  override def queryPull(itemCode: String, query: Map[String, String], source: MdsSourceItemDTO): Resp[Any] = {
    // TODO
    MdsContext.dataExchangeWorker.queryBySqlReq("", List())
  }

}
