package com.mdataset.worker.mock

import java.util.Date

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsIdModel, MdsSourceItemDTO}
import com.mdataset.lib.worker.basic.MdsAdapter
import com.mdataset.lib.worker.basic.annotation.{Entity, Family}

import scala.beans.BeanProperty

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

}

@Entity()
class Model extends MdsIdModel {

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
