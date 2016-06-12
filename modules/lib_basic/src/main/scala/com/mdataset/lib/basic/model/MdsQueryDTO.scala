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
case class MdsQueryFormatDTO(
                              field: String,
                              default_value: String,
                              data_type: String,
                              desc: String,
                              necessary: Boolean
                            )
