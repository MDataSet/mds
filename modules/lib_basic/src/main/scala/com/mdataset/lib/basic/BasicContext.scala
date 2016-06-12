package com.mdataset.lib.basic

/**
  * 基础上下文
  *
  * 用于定义公共常量
  */
object BasicContext {

  // ========= 定义与API Service 交互的主题 =========
  // API注册主题名
  val FLAG_API_REGISTER: String = "mds_api_register"
  // API注销主题名
  val FLAG_API_UN_REGISTER: String = "mds_api_unRegister"
  // API采集主题名前缀
  val FLAG_API_COLLECT_EXEC: String = "mds_api_collectExec_"
  // API采集测试主题名前缀
  val FLAG_API_COLLECT_TEST: String = "mds_api_collectTest_"

  // ========= 定义与BD Service 交互的主题 =========
  // 数据注册主题名
  val FLAG_DATA_REGISTER: String = "mds_data_register"
  // 数据注销主题名
  val FLAG_DATA_UN_REGISTER: String = "mds_data_unRegister"
  // 数据插入主题前缀
  val FLAG_DATA_INSERT: String = "mds_data_insert_"

}
