package com.mdataset.startup.model

import com.mdataset.lib.basic.model.MdsSourceItemDTO

import scala.beans.BeanProperty

class MdsSourceWithStatusVO extends MdsSourceItemDTO {

  @BeanProperty var code: String = _

  @BeanProperty var status: Boolean = _

  @BeanProperty var last_update_time: String = _

}
