package com.mdataset.service.bd.model

import com.ecfront.common.{BeanHelper, Resp}
import com.ecfront.ez.framework.service.storage.foundation._
import com.ecfront.ez.framework.service.storage.mongo.MongoStatusStorage
import com.mdataset.lib.basic.model.{MdsRegisterEntityMetaDTO, MdsRegisterReqDTO}
import com.mdataset.service.bd.MdsContext

import scala.beans.BeanProperty

/**
  * 数据源主体实体对象
  *
  */
class MdsRegisterReqEntity extends StatusModel {

  @Index
  @Unique
  @Label("数据源code")
  @BeanProperty var code: String = _
  @Label("实体信息")
  @BeanProperty var entityMeta: List[MdsRegisterEntityMetaDTO] = _

}

object MdsRegisterReqEntity extends MongoStatusStorage[MdsRegisterReqEntity] {

  def initCache(): Unit = {
    MdsRegisterReqEntity.findEnabled("").body.foreach {
      source =>
        val code = source.code
        MdsContext.sources += code -> MdsRegisterReqDTO(source.code, source.entityMeta)
    }
  }

  def saveOrUpdateWithCache(source: MdsRegisterReqDTO): Resp[Void] = {
    val code = source.code
    if (MdsContext.sources.contains(code) && MdsContext.sources(code).hashCode() != source.hashCode()) {
      Resp.conflict("数据源code重复")
    } else {
      val sourceEntity = new MdsRegisterReqEntity
      BeanHelper.copyProperties(sourceEntity, source)
      val storageSource = MdsRegisterReqEntity.getEnabledByCond(s"""{"code":"$code"}""").body
      if (storageSource != null) {
        sourceEntity.id = storageSource.id
      }
      MdsRegisterReqEntity.saveOrUpdate(sourceEntity)
      MdsContext.sources += code -> source
      Resp.success(null)
    }
  }

  def removeWithCache(code: String): Resp[Void] = {
    MdsRegisterReqEntity.deleteByCond(s"""{"code":"$code"}""")
    MdsContext.sources -= code
    Resp.success(null)
  }

}


