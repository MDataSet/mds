package com.mdataset.lib.core

import com.ecfront.common.JsonHelper
import com.ecfront.ez.framework.core.{EZContext, EZManager}
import com.ecfront.ez.framework.service.distributed.DMonitorService
import com.mdataset.lib.basic.model.MdsSourceDTO
import com.mdataset.lib.basic.{MdsAdapter, MdsExchangeAPI}
import com.typesafe.scalalogging.slf4j.LazyLogging

import scala.reflect.runtime._


object MdsStartup extends App with LazyLogging {

  private val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)

  private val FLAG_SOURCE: String = "source"

  if (EZManager.start()) {
    try {
      val adapterStr = s"com.mdataset.worker.${EZContext.module}.ServiceAdapter$$"
      MdsContext.adapter = runtimeMirror.reflectModule(runtimeMirror.staticModule(adapterStr)).instance.asInstanceOf[MdsAdapter]
    } catch {
      case e: Throwable =>
        logger.error(s"${EZContext.module} service adapter must instance of MdsAdapter.", e)
    }
    if (MdsContext.adapter != null) {
      try {
        MdsContext.source = JsonHelper.toObject(EZContext.args.getJsonObject(FLAG_SOURCE).encode(), classOf[MdsSourceDTO])
        MdsContext.source.code = EZContext.app + "_" + EZContext.module
      } catch {
        case e: Throwable =>
          logger.error(s"${EZContext.module} source parse error.", e)
      }
      if (MdsContext.source != null) {
        val initR = MdsContext.adapter.init(MdsContext.source)
        if (initR) {
          MdsExchangeProcessor.process()
          DMonitorService.start()
          logger.info(s"${EZContext.module} started.")
        } else {
          logger.error(s"${EZContext.module} init error [${initR.code}] ${initR.message}.")
        }
      }
    }
  }

  sys.addShutdownHook {
    val shutdownR = MdsContext.adapter.shutdown(MdsContext.source)
    if (shutdownR) {
      MdsExchangeAPI.Worker.unRegister(MdsContext.source.code)
      logger.info(s"${EZContext.module} shutdown.")
    } else {
      logger.error(s"${EZContext.module} shutdown error [${shutdownR.code}] ${shutdownR.message}.")
    }
  }


}
