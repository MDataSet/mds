package com.mdataset.lib.basic.model

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