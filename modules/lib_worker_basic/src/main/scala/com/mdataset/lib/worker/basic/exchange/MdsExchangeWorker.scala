package com.mdataset.lib.worker.basic.exchange

import com.ecfront.common.Resp
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
  * 公共交互接口封装
  */
trait MdsExchangeWorker extends LazyLogging {

  /**
    * 注销Worker请求
    *
    * @param code 数据源code
    */
  def unRegisterReq(code: String): Unit = {
    fetchUnRegisterReq(code)
  }

  /**
    * 注销Worker请求消息实现
    *
    * @param code 数据源code
    * @return 是否成功
    */
  protected def fetchUnRegisterReq(code: String): Resp[Void]

}

