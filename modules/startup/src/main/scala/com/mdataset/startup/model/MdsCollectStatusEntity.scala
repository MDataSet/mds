package com.mdataset.startup.model

import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.storage.foundation.{BaseModel, Entity, Index}
import com.ecfront.ez.framework.service.storage.mongo.MongoBaseStorage
import com.mdataset.lib.basic.model.MdsCollectStatusDTO

import scala.beans.BeanProperty

@Entity("Collect Status")
class MdsCollectStatusEntity extends BaseModel {

  @Index
  @BeanProperty var code: String = _

  @Index
  @BeanProperty var item_code: String = _

  @BeanProperty var status: Boolean = _

  @BeanProperty var last_update_time: String = _

  @BeanProperty var info: Map[String, Any] = _

}

object MdsCollectStatusEntity extends MongoBaseStorage[MdsCollectStatusEntity] {

  def getByCode(code: String, itemCode: String): MdsCollectStatusEntity = {
    MdsCollectStatusEntity.getByCond(s"""{"code":"$code","item_code":"$itemCode"}""").body
  }

  implicit def toVO(entity: MdsCollectStatusEntity): MdsCollectStatusDTO = {
    val vo = new MdsCollectStatusDTO
    vo.code = entity.code
    vo.item_code = entity.item_code
    vo.last_update_time = TimeHelper.msf.parse(entity.last_update_time)
    vo.info = entity.info
    vo
  }
}
