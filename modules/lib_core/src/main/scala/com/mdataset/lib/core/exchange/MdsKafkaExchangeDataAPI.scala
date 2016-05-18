package com.mdataset.lib.core.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor.Producer
import com.mdataset.lib.core.MdsContext

object MdsKafkaExchangeDataAPI extends MdsExchangeDataAPI {

  private val INSERT_TOPIC_PREFIX: String = "mds_insert_"

  private val producers = collection.mutable.Map[String, Producer]()

  override def insert(itemCode: String, lines: List[Any]): Resp[Void] = {
    if (!producers.contains(itemCode)) {
      producers += itemCode -> KafkaProcessor.Producer(INSERT_TOPIC_PREFIX + MdsContext.source.code + "_" + itemCode, MdsContext.source.code + "_" + itemCode)
    }
    val producer = producers(itemCode)
    if (lines != null) {
      lines.foreach {
        line =>
          producer.send(JsonHelper.toJsonString(line))
      }
      Resp.success(null)
    } else {
      Resp.success(null)
    }
  }
}

