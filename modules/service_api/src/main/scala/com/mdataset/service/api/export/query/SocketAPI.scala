package com.mdataset.service.api.export.query

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.core.EZContext
import com.mdataset.lib.basic.model.QueryReqDTO
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.process.MdsLimitProcessor
import com.typesafe.scalalogging.slf4j.LazyLogging
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.{NetServer, NetSocket}
import io.vertx.core.{AsyncResult, Handler}

/**
  * 数据API
  */
object SocketAPI extends LazyLogging {

  private val sockets= collection.mutable.Map[String,NetSocket]()

  def listening(port: Int, ip: String): Unit = {
    val socket=EZContext.vertx.createNetServer()
    socket.connectHandler(new Handler[NetSocket] {
      override def handle(soc: NetSocket): Unit = {
        soc.handler(new Handler[Buffer] {
          override def handle(buffer: Buffer): Unit = {
            val data = buffer.getString(0, buffer.length)
            try {
              val queryReq = JsonHelper.toObject[QueryReqDTO](data)
              if (queryReq.clientId == null || queryReq.clientId.isEmpty) {
                queryReq.clientId = soc.remoteAddress().host() + ":" + soc.remoteAddress().port()
              }
              if (queryReq.condition == null) {
                queryReq.condition = Map()
              }
              if (MdsContext.sources.contains(queryReq.sourceCode) && MdsContext.sources(queryReq.sourceCode).contains(queryReq.sourceItemCode)) {
                val item = MdsContext.sources(queryReq.sourceCode)(queryReq.sourceItemCode)
                val limitR = MdsLimitProcessor.limitFilter(queryReq.sourceCode, item)
                if (limitR) {
                  MdsContext.apiExchangeMaster.queryReq(queryReq)
                  sockets += getKey(queryReq.sourceCode, queryReq.sourceItemCode, queryReq.clientId) -> soc
                } else {
                  soc.write(JsonHelper.toJsonString(limitR))
                }
              } else {
                soc.write(JsonHelper.toJsonString(Resp.notImplemented(s"请求的[${queryReq.sourceCode}][${queryReq.sourceItemCode}]不存在")))
              }
            } catch {
              case e: Throwable =>
                logger.warn("Request data format error.", e)
            }
          }
        })
        soc.exceptionHandler(new Handler[Throwable] {
          override def handle(event: Throwable): Unit = {
            logger.error("Server error.", event)
          }
        }).closeHandler(new Handler[Void] {
          override def handle(event: Void): Unit = {
            logger.info("The socket has been closed")
          }
        })
      }
    })
    socket.listen(port, ip, new Handler[AsyncResult[NetServer]] {
      override def handle(res: AsyncResult[NetServer]): Unit = {
        if (res.succeeded()) {
          logger.info("Server is now listening on actual port: " + port)
        } else {
          logger.error("Failed to bind!", res.cause())
        }
      }
    })
  }

  def send(code: String, itemCode: String, clientId: String, message: String): Unit = {
    val key = getKey(code, itemCode, clientId)
    if (sockets.contains(key)) {
      sockets(key).write(message)
    }
  }

  def close(code: String, itemCode: String, clientId: String): Unit = {
    val key = getKey(code, itemCode, clientId)
    if (sockets.contains(key)) {
      sockets(key).close()
      sockets -= key
    }
  }

  private def getKey(code: String, itemCode: String, clientId: String): String = {
    code + "_" + itemCode + "_" + clientId
  }

}
