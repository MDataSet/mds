package com.mdataset.worker.mock

import java.util.Date

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceItemDTO, QueryReqDTO}
import com.mdataset.lib.worker.basic.model.{BDEntity, Family, MdsBaseEntity}
import com.mdataset.lib.worker.basic.{MdsAdapter, MdsWorkerBasicContext}

import scala.beans.BeanProperty

object ServiceAdapter extends MdsAdapter {

  override def collectExec(itemCode: String, source: MdsSourceItemDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO] = {
    status.last_update_time = new Date()
    MdsWorkerBasicContext.dataExchangeWorker
      .insertReq("model", Model("a", 20, enable = true))
    MdsWorkerBasicContext.dataExchangeWorker
      .insertReq("model", Model("b", 40, enable = true))
    MdsWorkerBasicContext.dataExchangeWorker
      .insertReq("model", Model("c", 20, enable = false))
    Resp.success(status)
  }

  override def collectTest(itemCode: String, source: MdsSourceItemDTO): Resp[Void] = {
    Resp.success(null)
  }

  override def query(itemCode: String, queryReq: QueryReqDTO, source: MdsSourceItemDTO): Resp[(String, List[Any])] = {
    Resp.success("SELECT * FROM model", List())
  }

}

@BDEntity()
class Model extends MdsBaseEntity {

  @Family("")
  @BeanProperty var name: String = _
  @Family("")
  @BeanProperty var age: Int = _
  @Family("opt")
  @BeanProperty var enable: Boolean = _

}

object Model {
  def apply(name: String, age: Int, enable: Boolean): Model = {
    val model = new Model()
    model.name = name
    model.age = age
    model.enable = enable
    model
  }
}
