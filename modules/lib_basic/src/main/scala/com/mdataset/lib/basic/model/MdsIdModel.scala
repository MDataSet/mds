package com.mdataset.lib.basic.model

import scala.beans.BeanProperty

/**
  * 数据持久化基类
  *
  * 所有需要持久化的数据实体都要继承此类
  */
class MdsIdModel {

  // 主键
  @BeanProperty var id: String = _

}
