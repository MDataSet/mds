package com.mdataset.worker.mock

import java.util.Date

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsIdModel, MdsSourceItemDTO, MdsSourceMainDTO}
import com.mdataset.lib.worker.basic.annotation.Entity
import com.mdataset.lib.worker.basic.{MdsAdapter, MdsContext}

object ServiceAdapter extends MdsAdapter {

  override def collectExec(itemCode: String, source: MdsSourceItemDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO] = {
    status.last_update_time = new Date()
    Resp.success(status)
  }

  override def collectTest(itemCode: String, source: MdsSourceItemDTO): Resp[Void] = {
    Resp.success(null)
  }

  override def queryPush(): Resp[(String, Any)] = {
    Thread.sleep(10000)
    Resp.success(("model",s"""{"t":1}"""))
  }

  override def queryPull(itemCode: String, query: Map[String, String], source: MdsSourceItemDTO): Resp[Any] = {
    Resp.success(s"""{"t":1}""")
  }

  override def init(source: MdsSourceMainDTO): Resp[Void] = {
    MdsContext.dataExchangeWorker.insert(List(
      Model("a", 20, enable = true),
      Model("b", 40, enable = true),
      Model("c", 20, enable = false)
    ))
    Thread.sleep(10000)
    val result = MdsContext.dataExchangeWorker.queryBySqlReq[Model]("SELECT * FROM model WHERE age > ?", List(20))
    println(result)
    super.init(source)
  }
}

@Entity()
case class Model(name: String, age: Int, enable: Boolean) extends MdsIdModel
