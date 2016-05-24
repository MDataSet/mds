package com.mdataset.lib.basic.model

case class MdsQuerySqlReqDTO(sql: String, parameters: List[Any])

case class MdsInsertReqDTO(tableName: String, items: List[String])

case class MdsRegisterReqDTO(code: String, entityMeta:List[MdsRegisterEntityMetaDTO])

/**
  * @param name entity name
  * @param fieldFamilies field Name  -> Family Name
  * @param fieldTypes field Name -> scala type
  */
case class MdsRegisterEntityMetaDTO(name: String, fieldFamilies:Map[String,String],fieldTypes:Map[String,String])

