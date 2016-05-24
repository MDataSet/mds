package com.mdataset.lib.basic.model

import scala.beans.BeanProperty

/**
  * 数据源主体对象
  *
  * 一个worker只能有一个主体数据源
  */
class MdsSourceMainDTO {

  // code，不同数据源不能重复
  // TODO 重复检查
  @BeanProperty var code: String = _
  // 数据源项目
  @BeanProperty var items: List[MdsSourceItemDTO] = _

}

/**
  * 数据源项目
  *
  * 一个worker可以有多个数据源项
  *
  * 如某worker是处理汽车数据的，它可以有 车型数据、经销商数据 等不同项目
  */
class MdsSourceItemDTO {

  // item code 同一个数据源中不能重复
  // TODO 重复检查
  @BeanProperty var item_code: String = _
  // 所属类别，按行业划分
  // TODO 建立类别字典
  @BeanProperty var category: String = _
  // 数据来源描述
  @BeanProperty var data_source: String = _
  // 采集调度周期
  @BeanProperty var collect_exec_schedule: String = _
  // 采集测试（可用性验证）调度周期
  @BeanProperty var collect_test_schedule: String = _
  // 采集认证对象
  @BeanProperty var collect_auth: MdsCollectAuthDTO = _
  // 推送响应格式
  @BeanProperty var query_push_resp_format: List[MdsQueryFormatDTO] = _
  // 查询请求格式
  @BeanProperty var query_pull_req_format: List[MdsQueryFormatDTO] = _
  // 查询响应格式
  @BeanProperty var query_pull_resp_format: List[MdsQueryFormatDTO] = _
  // 查询限制对象
  @BeanProperty var query_limit: MdsQueryLimitDTO = _
  // 数据描述
  @BeanProperty var desc: String = _
  // 扩展信息
  @BeanProperty var ext_info: Map[String, String] = _

}


