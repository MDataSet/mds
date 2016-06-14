package com.mdataset.lib.basic.model

import scala.beans.BeanProperty

/**
  * 向BD Service注册时传输的对象
  *
  * @param code       数据源code
  * @param entityMeta 实体信息
  */
case class MdsRegisterEntityReqDTO(@BeanProperty code: String,@BeanProperty entityMeta: List[MdsRegisterEntityMetaDTO])

/**
  * 实体信息
  */
class MdsRegisterEntityMetaDTO {

  @BeanProperty var name: String = _
  @BeanProperty var fieldFamilies: Map[String, String] = _
  @BeanProperty var fieldTypes: Map[String, String] = _

}

object MdsRegisterEntityMetaDTO {

  def apply(name: String, fieldFamilies: Map[String, String], fieldTypes: Map[String, String]): MdsRegisterEntityMetaDTO = {
    val dto = new MdsRegisterEntityMetaDTO()
    dto.name = name
    dto.fieldFamilies = fieldFamilies
    dto.fieldTypes = fieldTypes
    dto
  }

}

/**
  * 向BD Service插入数据的请求对象
  *
  * @param code     数据源code
  * @param itemCode 数据项code
  * @param data     数据
  */
case class MdsInsertReqDTO(code: String, itemCode: String, data: String)