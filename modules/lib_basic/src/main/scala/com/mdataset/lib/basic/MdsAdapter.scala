package com.mdataset.lib.basic

import com.ecfront.common.Resp
import com.mdataset.lib.basic.model.{MdsCollectStatusDTO, MdsSourceDTO}
import com.typesafe.scalalogging.slf4j.LazyLogging


trait MdsAdapter extends LazyLogging {

  def collectExec(source: MdsSourceDTO, status: MdsCollectStatusDTO): Resp[MdsCollectStatusDTO]

  def collectTest(source: MdsSourceDTO): Resp[Void]

  def queryPush(): Resp[Any]

  def queryPull(query: Map[String, String]): Resp[Any]

  def init(source: MdsSourceDTO): Resp[Void]

  def shutdown(source: MdsSourceDTO): Resp[Void]

}
