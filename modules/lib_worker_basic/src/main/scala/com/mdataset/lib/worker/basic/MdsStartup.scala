package com.mdataset.lib.worker.basic

import com.ecfront.common.{BeanHelper, ClassScanHelper, JsonHelper}
import com.ecfront.ez.framework.core.{EZContext, EZManager}
import com.ecfront.ez.framework.service.distributed.DMonitorService
import com.mdataset.lib.basic.model._
import com.mdataset.lib.worker.basic.annotation.{Entity, Family}
import com.typesafe.scalalogging.slf4j.LazyLogging
import io.vertx.core.json.JsonObject

import scala.reflect.runtime._

/**
  * Worker启动类
  */
object MdsStartup extends LazyLogging {

  private val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)

  // 配置文件中source内容的字段名
  private val FLAG_SOURCE: String = "source"

  def main(): Unit = {
    init(null)
  }

  /**
    * 初始化方法
    *
    * @param serviceAdapter 特殊的Worker服务适配器，为空时使用约定类名
    */
  def init(serviceAdapter: MdsAdapter = null): Unit = {
    if (EZManager.start()) {
      val basePath = s"com.mdataset.worker.${EZContext.module}"
      try {
        if (serviceAdapter != null) {
          val adapterStr = s"$basePath.ServiceAdapter$$"
          MdsWorkerBasicContext.adapter = runtimeMirror.reflectModule(runtimeMirror.staticModule(adapterStr)).instance.asInstanceOf[MdsAdapter]
        } else {
          MdsWorkerBasicContext.adapter = serviceAdapter
        }
      } catch {
        case e: Throwable =>
          logger.error(s"${EZContext.module} service adapter must instance of MdsAdapter.", e)
      }
      if (MdsWorkerBasicContext.adapter != null) {
        try {
          loadSource()
        } catch {
          case e: Throwable =>
            logger.error(s"${EZContext.module} source parse error.", e)
        }
        if (MdsWorkerBasicContext.source != null) {
          val initR = MdsWorkerBasicContext.adapter.init(MdsWorkerBasicContext.source)
          if (initR) {
            // 要注册的实体类meta信息
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
            // 启用各类交互接口
            MdsWorkerBasicContext.dataExchangeWorker.registerReq(MdsRegisterReqDTO(MdsWorkerBasicContext.source.code, entities))
            MdsWorkerBasicContext.apiExchangeWorker.registerReq(MdsWorkerBasicContext.source)
            MdsWorkerBasicContext.apiExchangeWorker.collectExecResp(MdsWorkerBasicContext.source.code)
            MdsWorkerBasicContext.apiExchangeWorker.collectTestResp(MdsWorkerBasicContext.source.code)
            MdsWorkerBasicContext.apiExchangeWorker.queryPullResp(MdsWorkerBasicContext.source.code)
            // 启动心跳服务
            DMonitorService.start()
            logger.info(s"${EZContext.module} started.")
          } else {
            logger.error(s"${EZContext.module} init error [${initR.code}] ${initR.message}.")
          }
        } else {
          logger.error(s"${EZContext.module} not found source config.")
        }
      } else {
        logger.error(s"${EZContext.module} not found adapter.")
      }
    }
  }

  /**
    * 加载数据源
    */
  private def loadSource(): Unit = {
    val sourceJson = EZContext.args.getJsonObject(FLAG_SOURCE)
    MdsWorkerBasicContext.source = JsonHelper.toObject[MdsSourceMainDTO](sourceJson.encode())
    MdsWorkerBasicContext.source.code = EZContext.app + "_" + EZContext.module
    MdsWorkerBasicContext.source.items.foreach {
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

  // 关闭处理
  sys.addShutdownHook {
    val shutdownR = MdsWorkerBasicContext.adapter.shutdown(MdsWorkerBasicContext.source)
    if (shutdownR) {
      MdsWorkerBasicContext.apiExchangeWorker.unRegisterReq(MdsWorkerBasicContext.source.code)
      MdsWorkerBasicContext.dataExchangeWorker.unRegisterReq(MdsWorkerBasicContext.source.code)
      logger.info(s"${EZContext.module} shutdown.")
    } else {
      logger.error(s"${EZContext.module} shutdown error [${shutdownR.code}] ${shutdownR.message}.")
    }
  }

}
