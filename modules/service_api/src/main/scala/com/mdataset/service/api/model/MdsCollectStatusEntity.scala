package com.mdataset.service.api.model

import com.ecfront.ez.framework.core.helper.TimeHelper
import com.ecfront.ez.framework.service.storage.foundation._
import com.ecfront.ez.framework.service.storage.mongo.MongoSecureStorage
import com.mdataset.lib.basic.model.MdsCollectStatusDTO

import scala.beans.BeanProperty

/**
  * 采集状态实体
  *
  * 用于保存各数据项的数据采集状态
  */
@Entity("Collect Status")
class MdsCollectStatusEntity extends SecureModel {

  @Index
  @Label("数据源code")
  @BeanProperty var code: String = _

  @Index
  @Label("数据项code")
  @BeanProperty var item_code: String = _

  @Label("最后一次执行是否成功")
  @BeanProperty var status: Boolean = _

  @Label("最后一次成功更新的时间")
  @BeanProperty var last_success_time: Long = _

  @Label("扩展信息")
  @BeanProperty var info: Map[String, Any] = _

}

object MdsCollectStatusEntity extends MongoSecureStorage[MdsCollectStatusEntity] {

  super.customTableName("source_status")

  def getByCode(code: String, itemCode: String): MdsCollectStatusEntity = {
    MdsCollectStatusEntity.getByCond(s"""{"code":"$code","item_code":"$itemCode"}""").body
  }

  implicit def toVO(entity: MdsCollectStatusEntity): MdsCollectStatusDTO = {
    val vo = new MdsCollectStatusDTO
    vo.code = entity.code
    vo.item_code = entity.item_code
    vo.last_update_time = TimeHelper.msf.parse(entity.last_success_time + "")
    vo.info = entity.info
    vo
  }
}
