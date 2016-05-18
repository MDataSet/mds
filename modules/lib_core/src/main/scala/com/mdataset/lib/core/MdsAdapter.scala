package com.mdataset.lib.core

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceItemDTO, MdsSourceMainDTO}
import com.typesafe.scalalogging.slf4j.LazyLogging


trait MdsAdapter extends LazyLogging {

  def collectExec(itemCode: String, source: MdsSourceItemDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO]

  def collectTest(itemCode: String, source: MdsSourceItemDTO): Resp[Void]

  def queryPush(): Resp[(String, Any)]

  def queryPull(itemCode: String, query: Map[String, String], source: MdsSourceItemDTO): Resp[Any]

  def init(source: MdsSourceMainDTO): Resp[Void] = Resp.success(null)

  def shutdown(source: MdsSourceMainDTO): Resp[Void] = Resp.success(null)

}
