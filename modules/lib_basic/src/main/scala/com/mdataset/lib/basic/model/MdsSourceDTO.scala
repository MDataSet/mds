package com.mdataset.lib.basic.model

import scala.beans.BeanProperty

class MdsSourceDTO {

  @BeanProperty var code: String = _

  @BeanProperty var category: String = _

  @BeanProperty var data_source: String = _

  @BeanProperty var collect_exec_schedule: String = _

  @BeanProperty var collect_test_schedule: String = _

  @BeanProperty var collect_auth: MdsCollectAuthDTO = _

  @BeanProperty var query_push_resp_format: List[MdsQueryFormatDTO] = _

  @BeanProperty var query_pull_req_format: List[MdsQueryFormatDTO] = _

  @BeanProperty var query_pull_resp_format: List[MdsQueryFormatDTO] = _

  @BeanProperty var query_limit: MdsQueryLimitDTO = _

  @BeanProperty var desc: String = _

  @BeanProperty var ext_info: Map[String, String] = _

}

