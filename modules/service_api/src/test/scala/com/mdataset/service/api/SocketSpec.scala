package com.mdataset.service.api

import java.util.concurrent.CountDownLatch

import com.ecfront.common.{JsonHelper, Resp, StandardCode}
import com.ecfront.ez.framework.core.EZContext
import com.mdataset.lib.basic.model.QueryReqDTO
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetSocket
import io.vertx.core.{AsyncResult, Handler}

class SocketSpec extends MockStartupSpec {

  test("Socket Test") {
    EZContext.vertx.createNetClient()
      .connect(MdsContext.socketPort, MdsContext.socketHost, new Handler[AsyncResult[NetSocket]] {
        override def handle(event: AsyncResult[NetSocket]): Unit = {
          val socket = event.result()
          socket.handler(new Handler[Buffer] {
            override def handle(event: Buffer): Unit = {
              val data = event.getString(0, event.length())
              println(data)
            }
          })
          socket.write(JsonHelper.toJsonString(QueryReqDTO("mock", "model", 20160501000000000L)))
        }
      })
    new CountDownLatch(1).await()
  }
}


