package com.mdataset.lib.basic.model

import scala.beans.BeanProperty

/**
  * 查询请求与响应格式对象
  *
  * @param field         字段名
  * @param default_value 默认值
  * @param data_type     类型（json类型）
  * @param desc          描述
  * @param necessary     是否必需
  */
// TODO 未实现
case class MdsQueryFormatDTO(
                              field: String,
                              default_value: String,
                              data_type: String,
                              desc: String,
                              necessary: Boolean
                            )

/**
  * 查询限制
  *
  * @param hourly_max_times 每小时最大查询次数
  */
case class MdsQueryLimitDTO(
                             hourly_max_times: Long
                           )

/**
  * 查询请求
  *
  * @param sourceCode 数据源code
  * @param sourceItemCode 数据项code
  * @param timeOffset 开始获取的时间
  * @param condition 查询条件
  * @param clientId 客户id
  */
case class QueryReqDTO(
                        @BeanProperty sourceCode: String,
                        @BeanProperty sourceItemCode: String,
                        @BeanProperty var timeOffset: Long,
                        @BeanProperty var condition: Map[String, Any] = Map(),
                        @BeanProperty var clientId: String = ""
                      )