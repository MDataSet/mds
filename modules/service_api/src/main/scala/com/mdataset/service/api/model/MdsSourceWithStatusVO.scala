package com.mdataset.service.api.model

import com.mdataset.lib.basic.model.MdsSourceItemDTO

import scala.beans.BeanProperty

/**
  * 采集状态VO
  */
class MdsSourceWithStatusVO extends MdsSourceItemDTO {

  @BeanProperty var code: String = _

  @BeanProperty var status: Boolean = _

  @BeanProperty var last_success_time: String = _

  @BeanProperty var last_check_time: String = _

}
