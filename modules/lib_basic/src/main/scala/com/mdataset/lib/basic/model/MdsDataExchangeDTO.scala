package com.mdataset.lib.basic.model

/**
  * 向BD Service注册时传输的对象
  *
  * @param code       数据源code
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
  * 向BD Service插入数据的请求对象
  *
  * @param code     数据源code
  * @param itemCode 数据项code
  * @param data     数据
  */
case class MdsInsertReqDTO(code: String, itemCode: String, data: String)

/**
  * 向BD Service查询数据的请求对象
  *
  * @param code       数据源code
  * @param itemCode   数据项code
  * @param clientId   请求Id
  * @param sql        sql
  * @param parameters 参数
  */
case class MdsQuerySqlReqDTO(code: String, itemCode: String, clientId: String, sql: String, parameters: List[Any])

/**
  * 向API Service返回数据的响应对象
  *
  * @param code     数据源code
  * @param itemCode 数据项code
  * @param clientId 请求Id
  * @param data     数据
  */
case class MdsQueryORPushRespDTO(code: String, itemCode: String, clientId: String, data: String)