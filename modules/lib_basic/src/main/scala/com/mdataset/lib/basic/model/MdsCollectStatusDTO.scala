package com.mdataset.lib.basic.model

import java.util.Date

import scala.beans.BeanProperty

class MdsCollectStatusDTO {

  @BeanProperty var code: String = _

  @BeanProperty var item_code: String = _

  @BeanProperty var info: Map[String, Any] = _

  @BeanProperty var last_update_time: Date = _

}
