package com.mdataset.worker.autoinfo

import com.ecfront.common.JsonHelper
import com.mdataset.lib.core.helper.HttpHelper
import com.mdataset.worker.autoinfo.model.ModelAutoInfo
import org.jsoup.Jsoup

import scala.collection.JavaConversions._
import scala.language.implicitConversions

object CollectProcessor {

  private val baseUrl = "http://www.autohome.com.cn/"
  private implicit val context: String = "text/html, text/html; charset=gb2312"

  def collect(): Unit = {
    val modelAutoInfos = ('A' to 'Z').map {
      i =>
        val brandHtml = HttpHelper.get(baseUrl + s"grade/carhtml/$i.html")
        Jsoup.parse(brandHtml).select("dl").map {
          $brand =>
            val brandName = $brand.select("dt").text()
            // val brandImage = $brand.find("dt img").attr("src")
            $brand.select("dd").map {
              $company =>
                val companyName = $company.select(".h3-tit").text()
                $company.select("li").map {
                  $series =>
                    val seriesName = $series.select("h4").text()
                    val seriesDetailUrl = $series.select("h4 a").attr("href")
                    val $seriesDetail = Jsoup.parse(HttpHelper.get(seriesDetailUrl))
                    $seriesDetail.select(".interval01-list-related div").map {
                      modelLinkInfo =>
                        val modelConfigUrl = baseUrl + modelLinkInfo.select("a").eq(3).attr("href")
                        val detail = HttpHelper.get(modelConfigUrl)
                        val $detail = Jsoup.parse(detail)
                        val config = parseConfig(detail)
                        val modelAutoInfo = new ModelAutoInfo
                        modelAutoInfo.brand_name = brandName
                        modelAutoInfo.company_name = companyName
                        modelAutoInfo.series_name = seriesName
                        modelAutoInfo.model_name = $detail.select(".subnav-title-name h1").text()
                        modelAutoInfo.company_price = config("厂商指导价(元)").replace("万", "")
                        // TODO modelAutoInfo.dealer_price = config("经销商参考价").replace("万", "").toFloat
                        modelAutoInfo.basic_category = config("级别")
                        modelAutoInfo.basic_engine = config("发动机")
                        modelAutoInfo.basic_transmission_box = config("变速箱")
                        modelAutoInfo.basic_volume = config("长*宽*高(mm)")
                        modelAutoInfo.basic_structure = config("车身结构")
                        modelAutoInfo.basic_max_speed = config("最高车速(km/h)")
                        modelAutoInfo.basic_official_acceleration = config("官方0-100km/h加速(s)")
                        modelAutoInfo.basic_real_acceleration = config("实测0-100km/h加速(s)")
                        modelAutoInfo.basic_real_brake = config("实测100-0km/h制动(m)")
                        modelAutoInfo.basic_real_oil_consumption = config("实测油耗(L/100km)")
                        modelAutoInfo.basic_official_oil_consumption = config("工信部综合油耗(L/100km)")
                        modelAutoInfo.basic_ground_clearance = config("实测离地间隙(mm)")
                        modelAutoInfo.basic_warranty = config("整车质保")
                        modelAutoInfo
                    }
                }
            }
        }
    }
    // TODO save
    println("save it.")
  }

  private def parseConfig(detail: String): Map[String, String] = {
    val configLine = detail.split("\r\n").find(_.trim.startsWith("var config =")).get.trim
    val config = JsonHelper.toJson(configLine.substring("var config =".length)).get("result").get("paramtypeitems")

    val configOptLine = detail.split("\r\n").find(_.trim.startsWith("var option =")).get.trim
    val configOpt = JsonHelper.toJson(configOptLine.substring("var option =".length)).get("result").get("configtypeitems")
    config.flatMap {
      _.get("paramitems").map {
        item =>
          val value = item.get("valueitems").head.get("value").asText() match {
            case "-" => ""
            case v => v
          }
          item.get("name").asText() -> value
      }
    }.toMap ++
      configOpt.flatMap {
        _.get("configitems").map {
          item =>
            val value = item.get("valueitems").head.get("value").asText() match {
              case v if v.contains("●") => "Y"
              case v if v.contains("○") => "O"
              case "-" => "N"
                // TODO like 前● / 后●
              case v => v
            }
            item.get("name").asText() -> value
        }
      }.toMap
  }

  implicit def strToFloat(str: String): Float = {
    if (str.trim.isEmpty) {
      0
    } else {
      new java.lang.Float(str.trim)
    }
  }

  implicit def strToLong(str: String): Long = {
    if (str.trim.isEmpty) {
      0
    } else {
      new java.lang.Long(str.trim)
    }
  }

}
