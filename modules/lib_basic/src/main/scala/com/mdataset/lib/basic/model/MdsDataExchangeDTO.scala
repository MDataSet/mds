package com.mdataset.lib.basic.model

import scala.beans.BeanProperty

/**
  * 向BD Service注册时传输的对象
  *
  * @param code       数据源code
  * @param entityMeta 实体信息
  */
case class MdsRegisterEntityReqDTO(code: String, entityMeta: List[MdsRegisterEntityMetaDTO])

/**
  * 实体信息
  *
  * @param name          entity name
  * @param fieldFamilies field Name  -> Family Name
  * @param fieldTypes    field Name -> scala type
*/
case class MdsRegisterEntityMetaDTO(
                                     @BeanProperty var name: String,
                                     @BeanProperty var fieldFamilies: Map[String, String],
                                     @BeanProperty var fieldTypes: Map[String, String]
                                   )

/**
  * 向BD Service插入数据的请求对象
  *
  * @param code     数据源code
  * @param itemCode 数据项code
  * @param data     数据
  */
case class MdsInsertReqDTO(code: String, itemCode: String, data: String)