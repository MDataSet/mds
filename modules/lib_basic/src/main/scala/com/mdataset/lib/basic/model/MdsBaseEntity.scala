package com.mdataset.lib.basic.model

import scala.beans.BeanProperty

/**
  * 大数据持久化基类
  *
  * 所有需要持久化的大数据实体都要继承此类
  */
class MdsBaseEntity {
  // 主键
  @BeanProperty var id: String = _
  // 更新时间（yyyyMMddHHmmssSSS）
  @BeanProperty var update_time: Long = _

}
