package com.mdataset.worker.autoinfo

import java.util.Date

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceItemDTO}
import com.mdataset.lib.worker.basic.MdsAdapter

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

}
