package com.mdataset.service.api.model

import scala.beans.BeanProperty

/**
  * 查询请求
  *
  * @param sourceCode     数据源code
  * @param sourceItemCode 数据项code
  * @param condition      查询条件
  * @param clientId       客户id
  */
case class MdsQueryDTO(
                        @BeanProperty sourceCode: String,
                        @BeanProperty sourceItemCode: String,
                        @BeanProperty var condition: Map[String, Any] = Map(),
                        @BeanProperty var clientId: String = ""
                      )