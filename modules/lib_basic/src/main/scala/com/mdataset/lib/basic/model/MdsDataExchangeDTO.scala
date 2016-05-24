package com.mdataset.lib.basic.model

/**
  * 向BD Service注册时传输的对象
  *
  * @param code       code
  * @param entityMeta 实体信息
  */
case class MdsRegisterReqDTO(code: String, entityMeta: List[MdsRegisterEntityMetaDTO])

/**
  * 实体信息
  *
  * @param name          entity name
  * @param fieldFamilies field Name  -> Family Name
  * @param fieldTypes    field Name -> scala type
  */
case class MdsRegisterEntityMetaDTO(name: String, fieldFamilies: Map[String, String], fieldTypes: Map[String, String])

/**
  * 向BD Service查询数据的请求对象
  *
  * @param sql        sql
  * @param parameters 参数
  */
case class MdsQuerySqlReqDTO(sql: String, parameters: List[Any])

/**
  * 向BD Service插入数据的请求对象
  *
  * @param tableName 表名
  * @param items     数据项
  */
case class MdsInsertReqDTO(tableName: String, items: List[String])
