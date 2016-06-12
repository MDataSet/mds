package com.mdataset.lib.worker.basic

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceItemDTO, MdsSourceMainDTO}
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
  * Worker服务适配器
  *
  * Worker的执行入口类
  */
trait MdsAdapter extends LazyLogging {

  /**
    * 数据采集执行方法，此方法由API Service调度模块通过MQ触发
    *
    * @param itemCode 要采集的item code
    * @param source   要采集的数据项
    * @param status   当前状态（最后一次执行的信息）
    * @return 新的状态（当前的信息，其中last_update_time必须更新）
    */
  def collectExec(itemCode: String, source: MdsSourceItemDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO]

  /**
    * 数据采集测试方法，此方法由API Service调度模块通过MQ触发
    *
    * @param itemCode 要测试的item code
    * @param source   要测试的数据项
    * @return 是否成功
    */
  def collectTest(itemCode: String, source: MdsSourceItemDTO): Resp[Void]

  /**
    * 初始化方法
    *
    * @param source 数据源主体
    * @return 是否成功
    */
  def init(source: MdsSourceMainDTO): Resp[Void] = Resp.success(null)

  /**
    * 销毁方法
    *
    * @param source 数据源主体
    * @return 是否成功
    */
  def shutdown(source: MdsSourceMainDTO): Resp[Void] = Resp.success(null)

}
