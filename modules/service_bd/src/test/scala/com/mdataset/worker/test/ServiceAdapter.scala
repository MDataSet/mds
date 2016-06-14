package com.mdataset.worker.test

import java.util.Date

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceItemDTO}
import com.mdataset.lib.worker.basic.model.{BDEntity, Family, MdsBaseEntity}
import com.mdataset.lib.worker.basic.{MdsAdapter, MdsWorkerBasicContext}

import scala.beans.BeanProperty

object ServiceAdapter extends MdsAdapter {

  override def collectExec(itemCode: String, source: MdsSourceItemDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO] = {
    status.last_update_time = new Date()
    itemCode match {
      case "item1" =>
        MdsWorkerBasicContext.dataExchangeWorker.insertReq(itemCode, Item1Model("a", 20, enable = true))
        MdsWorkerBasicContext.dataExchangeWorker.insertReq(itemCode, Item1Model("b", 40, enable = true))
        MdsWorkerBasicContext.dataExchangeWorker.insertReq(itemCode, Item1Model("c", 20, enable = false))
      case "item2" =>
        MdsWorkerBasicContext.dataExchangeWorker.insertReq(itemCode, Item2Model("t1", 20, "xxxxxx", enable = true))
        MdsWorkerBasicContext.dataExchangeWorker.insertReq(itemCode, Item2Model("t2", 40, "yyyyyy", enable = true))
        MdsWorkerBasicContext.dataExchangeWorker.insertReq(itemCode, Item2Model("t3", 20, "zzzzzz", enable = false))
    }
    Resp.success(status)
  }

  override def collectTest(itemCode: String, source: MdsSourceItemDTO): Resp[Void] = {
    Resp.success(null)
  }

}

@BDEntity()
class Item1Model extends MdsBaseEntity {

  @Family("")
  @BeanProperty var name: String = _
  @Family("")
  @BeanProperty var age: Int = _
  @Family("opt")
  @BeanProperty var enable: Boolean = _

}

object Item1Model {
  def apply(name: String, age: Int, enable: Boolean): Item1Model = {
    val model = new Item1Model()
    model.name = name
    model.age = age
    model.enable = enable
    model
  }
}

@BDEntity()
class Item2Model extends MdsBaseEntity {

  @Family("")
  @BeanProperty var title: String = _
  @Family("")
  @BeanProperty var category: Int = _
  @Family("")
  @BeanProperty var content: String = _
  @Family("")
  @BeanProperty var enable: Boolean = _

}

object Item2Model {
  def apply(title: String, category: Int, content: String, enable: Boolean): Item2Model = {
    val model = new Item2Model()
    model.title = title
    model.category = category
    model.content = content
    model.enable = enable
    model
  }
}