package com.mdataset.lib.worker.basic

import com.ecfront.common.{BeanHelper, ClassScanHelper, JsonHelper}
import com.ecfront.ez.framework.core.{EZContext, EZManager}
import com.ecfront.ez.framework.service.distributed.DMonitorService
import com.mdataset.lib.basic.model._
import com.mdataset.lib.worker.basic.annotation.{Entity, Family}
import com.typesafe.scalalogging.slf4j.LazyLogging
import io.vertx.core.json.JsonObject

import scala.reflect.runtime._

object MdsStartup extends LazyLogging {

  private val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)

  private val FLAG_SOURCE: String = "source"

  def main(): Unit = {
    init(null)
  }

  def init(serviceAdapter: MdsAdapter = null): Unit = {
    if (EZManager.start()) {
      val basePath = s"com.mdataset.worker.${EZContext.module}"
      try {
        if (serviceAdapter != null) {
          val adapterStr = s"$basePath.ServiceAdapter$$"
          MdsContext.adapter = runtimeMirror.reflectModule(runtimeMirror.staticModule(adapterStr)).instance.asInstanceOf[MdsAdapter]
        } else {
          MdsContext.adapter = serviceAdapter
        }
      } catch {
        case e: Throwable =>
          logger.error(s"${EZContext.module} service adapter must instance of MdsAdapter.", e)
      }
      if (MdsContext.adapter != null) {
        try {
          loadSource()
        } catch {
          case e: Throwable =>
            logger.error(s"${EZContext.module} source parse error.", e)
        }
        if (MdsContext.source != null) {
          val initR = MdsContext.adapter.init(MdsContext.source)
          if (initR) {
            val entities =
              ClassScanHelper.scan[Entity](basePath).map {
                clazz =>
                  val tableName = clazz.getSimpleName.toLowerCase
                  val fieldTypes = BeanHelper.findFields(clazz)
                  val fieldFamilies = BeanHelper.findFieldAnnotations(clazz, Seq(classOf[Family])).map {
                    item =>
                      item.fieldName -> item.annotation.asInstanceOf[Family].name
                  }.toMap
                  MdsRegisterEntityMetaDTO(tableName, fieldFamilies, fieldTypes)
              }
            MdsContext.dataExchangeWorker.registerReq(MdsRegisterReqDTO(MdsContext.source.code, entities))
            MdsContext.apiExchangeWorker.registerReq(MdsContext.source)
            MdsContext.apiExchangeWorker.collectExecResp(MdsContext.source.code)
            MdsContext.apiExchangeWorker.collectTestResp(MdsContext.source.code)
            MdsContext.apiExchangeWorker.queryPullResp(MdsContext.source.code)
            MdsContext.apiExchangeWorker.queryPushReq()
            DMonitorService.start()
            logger.info(s"${EZContext.module} started.")
          } else {
            logger.error(s"${EZContext.module} init error [${initR.code}] ${initR.message}.")
          }
        }
      }
    }
  }

  private def loadSource(): Unit = {
    val sourceJson = EZContext.args.getJsonObject(FLAG_SOURCE)
    MdsContext.source = JsonHelper.toObject[MdsSourceMainDTO](sourceJson.encode())
    MdsContext.source.code = EZContext.app + "_" + EZContext.module
    MdsContext.source.items.foreach {
      item =>
        if (item.category == null) {
          item.category = sourceJson.getString("category", "")
        }
        if (item.data_source == null) {
          item.data_source = sourceJson.getString("data_source", "")
        }
        if (item.collect_exec_schedule == null) {
          item.collect_exec_schedule = sourceJson.getString("collect_exec_schedule", "")
        }
        if (item.collect_test_schedule == null) {
          item.collect_test_schedule = sourceJson.getString("collect_test_schedule", "")
        }
        if (item.collect_auth == null) {
          item.collect_auth = JsonHelper.toObject[MdsCollectAuthDTO](sourceJson.getJsonObject("collect_auth", new JsonObject()).encode())
        }
        if (item.query_limit == null) {
          item.query_limit = JsonHelper.toObject[MdsQueryLimitDTO](sourceJson.getJsonObject("query_limit", new JsonObject()).encode())
        }
        if (item.ext_info == null) {
          item.ext_info = JsonHelper.toObject[Map[String, String]](sourceJson.getJsonObject("ext_info", new JsonObject()).encode())
        }
    }
  }

  sys.addShutdownHook {
    val shutdownR = MdsContext.adapter.shutdown(MdsContext.source)
    if (shutdownR) {
      MdsContext.apiExchangeWorker.unRegisterReq(MdsContext.source.code)
      MdsContext.dataExchangeWorker.unRegisterReq(MdsContext.source.code)
      logger.info(s"${EZContext.module} shutdown.")
    } else {
      logger.error(s"${EZContext.module} shutdown error [${shutdownR.code}] ${shutdownR.message}.")
    }
  }

}
