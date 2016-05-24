package com.mdataset.lib.basic.model

import java.util.Date

import scala.beans.BeanProperty

/**
  * 数据采集认证对象
  *
  * @param user_name     用户名（基于用户名与密码的认证）
  * @param password      密码（基于用户名与密码的认证）
  * @param account_token Token（基于Token的认证）
  * @param ssh_key       ssh key（基于公钥的认证）
  */
// TODO 未实现
case class MdsCollectAuthDTO(
                              user_name: String,
                              password: String,
                              account_token: String,
                              ssh_key: String
                            )

/**
  * 数据采集状态对象
  */
class MdsCollectStatusDTO {
  // code
  @BeanProperty var code: String = _
  // item code
  @BeanProperty var item_code: String = _
  // 当前信息（如最后一次更新的主键ID）
  @BeanProperty var info: Map[String, Any] = _
  // 最后一次更新时间
  @BeanProperty var last_update_time: Date = _

}