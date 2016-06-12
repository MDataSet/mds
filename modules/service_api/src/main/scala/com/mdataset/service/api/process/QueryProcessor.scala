package com.mdataset.service.api.process

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.core.EZContext
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor.Consumer
import com.mdataset.lib.basic.BasicContext
import com.mdataset.lib.basic.model.MdsInsertReqDTO
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.model.MdsQueryDTO
import com.typesafe.scalalogging.slf4j.LazyLogging
import io.vertx.core.net.NetSocket

/**
  * 采集执行调度器回调类
  */
object QueryProcessor extends LazyLogging {

  private val mqClient = collection.mutable.Map[String, Consumer]()
  private val socketClient = collection.mutable.Map[String, NetSocket]()


  def initSocket(socket: NetSocket, queryReq: MdsQueryDTO): Unit = {
    if (MdsContext.sources.contains(queryReq.sourceCode)
      && (queryReq.sourceItemCode == null || MdsContext.sources(queryReq.sourceCode).contains(queryReq.sourceItemCode))) {
      val socketId = getSocketId(socket)
      socketClient += socketId -> socket
      val consumer = KafkaProcessor.Consumer(BasicContext.FLAG_DATA_INSERT + queryReq.sourceCode, EZContext.module + "_" + queryReq.clientId)
      consumer.receive({
        (message, _) =>
          val insertReq = JsonHelper.toObject[MdsInsertReqDTO](message)
          if (queryReq.sourceItemCode == null || insertReq.itemCode == queryReq.sourceItemCode) {
            socket.write(message)
          }
          Resp.success(null)
      })
      mqClient += socketId -> consumer
    } else {
      socket.write(JsonHelper.toJsonString(Resp.notImplemented(s"请求的[${queryReq.sourceCode}][${queryReq.sourceItemCode}]不存在")))
    }
  }

  def removeSocket(socket: NetSocket): Unit = {
    val socketId = getSocketId(socket)
    if (mqClient.contains(socketId)) {
      mqClient(socketId).close()
      mqClient -= socketId
    }
    if (socketClient.contains(socketId)) {
      socketClient(socketId).close()
      socketClient -= socketId
    }
  }


  private def getSocketId(socket: NetSocket): String = {
    socket.remoteAddress().host() + ":" + socket.remoteAddress().port()
  }

}
