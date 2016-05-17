package com.mdataset.worker.autoinfo

import com.ecfront.common.Resp
import com.mdataset.lib.basic.MdsAdapter
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceDTO}

object ServiceAdapter extends MdsAdapter {

  override def collectExec(source: MdsSourceDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO] = {
    CollectProcessor.collect()
    Resp.success(status)
  }

  override def collectTest(source: MdsSourceDTO): Resp[Void] = {
    Resp.success(null)
  }

  override def queryPush(): Resp[Any] = {
    Thread.sleep(10000)
    Resp.success(s"""{"t":1}""")
  }

  override def queryPull(query: Map[String, String]): Resp[Any] = {
    Resp.success(query)
  }

  override def init(source: MdsSourceDTO): Resp[Void] = {
    Resp.success(null)
  }

  override def shutdown(source: MdsSourceDTO): Resp[Void] = {
    Resp.success(null)
  }

}
