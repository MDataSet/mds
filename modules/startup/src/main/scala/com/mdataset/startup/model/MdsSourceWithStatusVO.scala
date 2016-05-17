package com.mdataset.startup.model

import com.mdataset.lib.basic.model.MdsSourceDTO

import scala.beans.BeanProperty

class MdsSourceWithStatusVO extends MdsSourceDTO {

  @BeanProperty var status: Boolean = _

  @BeanProperty var last_update_time: String = _

}
