package com.mdataset.service.api.export.query

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.core.EZContext
import com.mdataset.service.api.MdsContext
import com.mdataset.service.api.model.MdsQueryDTO
import com.mdataset.service.api.process.QueryProcessor
import com.typesafe.scalalogging.slf4j.LazyLogging
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.{NetServer, NetSocket}
import io.vertx.core.{AsyncResult, Handler}

/**
  * 数据API
  */
object SocketAPI extends LazyLogging {

  def listening(port: Int, ip: String): Unit = {
    val socket = EZContext.vertx.createNetServer()
    socket.connectHandler(new Handler[NetSocket] {
      override def handle(soc: NetSocket): Unit = {
        soc.handler(new Handler[Buffer] {
          override def handle(buffer: Buffer): Unit = {
            val data = buffer.getString(0, buffer.length)
            try {
              val queryReq = JsonHelper.toObject[MdsQueryDTO](data)
              QueryProcessor.initSocket(soc,queryReq)
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
            QueryProcessor.removeSocket(soc)
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

}
