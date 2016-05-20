package com.mdataset.lib.core.exchange

import com.ecfront.common.{JsonHelper, Resp}
import com.ecfront.ez.framework.service.kafka.KafkaProcessor
import com.ecfront.ez.framework.service.kafka.KafkaProcessor.Producer
import com.mdataset.lib.core.MdsContext

object MdsKafkaExchangeDataAPI extends MdsExchangeDataAPI {

  private val INSERT_TOPIC_PREFIX: String = "mds_insert_"
  private val QUERY_SQL_REQ_TOPIC_PREFIX: String = "mds_query_sql_req_"
  private val QUERY_SQL_RESP_TOPIC_PREFIX: String = "mds_query_sql_resp_"

  private val producers = collection.mutable.Map[String, collection.mutable.Map[String, Producer]]()

  override protected def fetchInsert(itemCode: String, lines: List[Any]): Resp[Void] = {
    val producer = createAndGetProducer(itemCode, INSERT_TOPIC_PREFIX)
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

  override protected def fetchQueryBySql(itemCode: String, sql: String, parameters: Map[String, Any]): Resp[List[String]] = {
    val producer = createAndGetProducer(itemCode, QUERY_SQL_REQ_TOPIC_PREFIX)
    val result = producer.ack(JsonHelper.toJsonString(QuerySqlReq(sql, parameters)), QUERY_SQL_RESP_TOPIC_PREFIX + MdsContext.source.code + "_" + itemCode)
    if (result) {
      Resp.success(JsonHelper.toObject[List[String]](result.body))
    } else {
      result
    }
  }


  private def createAndGetProducer(itemCode: String, topicPrefix: String): Producer = {
    if (!producers.contains(itemCode)) {
      producers += itemCode -> collection.mutable.Map[String, Producer]()
    }
    if (!producers(itemCode).contains(topicPrefix)) {
      producers(itemCode) +=
        topicPrefix -> KafkaProcessor.Producer(topicPrefix + MdsContext.source.code + "_" + itemCode, MdsContext.source.code + "_" + itemCode)
    }
    producers(itemCode)(topicPrefix)
  }

  case class QuerySqlReq(sql: String, parameters: Map[String, Any])

}

