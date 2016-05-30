package com.mdataset.service.api.model

import com.ecfront.common.{BeanHelper, Resp}
import com.ecfront.ez.framework.service.storage.foundation._
import com.ecfront.ez.framework.service.storage.mongo.MongoStatusStorage
import com.mdataset.lib.basic.model.{MdsSourceItemDTO, MdsSourceMainDTO}
import com.mdataset.service.api.MdsContext

import scala.beans.BeanProperty

/**
  * 数据源主体实体对象
  *
  */
class MdsSourceMainEntity extends StatusModel {

  @Index
  @Unique
  @Label("数据源code")
  @BeanProperty var code: String = _
  @Label("数据源项目")
  @BeanProperty var items: List[MdsSourceItemDTO] = _

}

object MdsSourceMainEntity extends MongoStatusStorage[MdsSourceMainEntity] {

  def initCache(): Unit = {
    MdsSourceMainEntity.findEnabled("").body.foreach {
      source =>
        val code = source.code
        val value = source.items.map(i => i.item_code -> i).toMap
        MdsContext.sources += code -> value
    }
  }

  def saveOrUpdateWithCache(source: MdsSourceMainDTO): Resp[Void] = {
    val code = source.code
    // 排重
    val value = source.items.map(i => i.item_code -> i).toMap
    if (MdsContext.sources.contains(code) && MdsContext.sources(code).hashCode() != value.hashCode()) {
      Resp.conflict("数据源code重复")
    } else {
      val sourceEntity = new MdsSourceMainEntity
      BeanHelper.copyProperties(sourceEntity, source)
      val storageSource = MdsSourceMainEntity.getEnabledByCond(s"""{"code":"$code"}""").body
      if (storageSource != null) {
        sourceEntity.id = storageSource.id
      }
      MdsSourceMainEntity.saveOrUpdate(sourceEntity)
      MdsContext.sources += code -> value
      Resp.success(null)
    }
  }

  def removeWithCache(code: String): Resp[Void] = {
    MdsSourceMainEntity.deleteByCond(s"""{"code":"$code"}""")
    MdsContext.sources -= code
    Resp.success(null)
  }

}


