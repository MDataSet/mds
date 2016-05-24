package com.mdataset.worker.autoinfo

import com.ecfront.common.{JsonHelper, Resp}
import com.mdataset.lib.worker.basic.MdsWorkerBasicContext
import com.mdataset.lib.worker.basic.helper.{Charset, HttpHelper}
import com.mdataset.worker.autoinfo.model.ModelAutoInfo
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.jsoup.Jsoup

import scala.collection.JavaConversions._
import scala.language.implicitConversions

object CollectProcessor extends LazyLogging {

  private val baseUrl = "http://www.autohome.com.cn/"
  private implicit val context = "text/html; charset=gb2312"
  private implicit val charset = Charset.GB2312

  val ITEM_CODE_MODEL: String = "model"

  def collectModel(): Resp[Void] = {
    ('A' to 'Z').foreach {
      i =>
        // 1. 品牌分类列表，例如：autohome.com.cn/grade/carhtml/B.html
        val brandHtml = HttpHelper.get(baseUrl + s"grade/carhtml/$i.html")
        Jsoup.parse(brandHtml).select("dl").foreach {
          $brand =>
            val brandName = $brand.select("dt").text()
            // val brandImage = $brand.find("dt img").attr("src")
            $brand.select("dd").foreach {
              $company =>
                val companyName = $company.select(".h3-tit").text()
                val seriesObjects = $company.select("li:has(h4)").flatMap {
                  $series =>
                    val seriesId = $series.attr("id").replace("s", "")
                    val seriesName = $series.select("h4").text()
                    // 2. 车系配置详细页面 例如：http://car.autohome.com.cn/config/series/467.html
                    val seriesDetailUrl = s"http://car.autohome.com.cn/config/series/$seriesId.html"
                    val detail = HttpHelper.get(seriesDetailUrl)
                    val $detail = Jsoup.parse(detail)
                    val configList = parseConfig(detail)
                    configList.map {
                      config =>
                        val modelAutoInfo = new ModelAutoInfo
                        try {
                          modelAutoInfo.brand_name = brandName
                          modelAutoInfo.company_name = companyName
                          modelAutoInfo.series_name = seriesName
                          modelAutoInfo.model_name = $detail.select(".subnav-title-name h1").text()
                          val companyPrices = config.get("厂商指导价(元)").replace("万", "").split("~")
                          modelAutoInfo.company_price_min = companyPrices(0)
                          modelAutoInfo.company_price_min = if (companyPrices.length == 2) companyPrices(1) else companyPrices(0)
                          // TODO modelAutoInfo.dealer_price = config.get("经销商参考价").replace("万", "").toFloat
                          // 基本参数
                          modelAutoInfo.basic_category = config.get("级别")
                          modelAutoInfo.basic_engine = config.get("发动机")
                          modelAutoInfo.basic_transmission_box = config.get("变速箱")
                          modelAutoInfo.basic_volume = config.get("长*宽*高(mm)")
                          modelAutoInfo.basic_structure = config.get("车身结构")
                          modelAutoInfo.basic_max_speed = config.get("最高车速(km/h)")
                          modelAutoInfo.basic_official_acceleration = config.get("官方0-100km/h加速(s)")
                          modelAutoInfo.basic_real_acceleration = config.get("实测0-100km/h加速(s)")
                          modelAutoInfo.basic_real_brake = config.get("实测100-0km/h制动(m)")
                          modelAutoInfo.basic_real_oil_consumption = config.get("实测油耗(L/100km)")
                          modelAutoInfo.basic_official_oil_consumption = config.get("工信部综合油耗(L/100km)")
                          modelAutoInfo.basic_ground_clearance = config.get("实测离地间隙(mm)")
                          modelAutoInfo.basic_warranty = config.get("整车质保")
                          // 车身
                          modelAutoInfo.body_length = config.get("长度(mm)")
                          modelAutoInfo.body_width = config.get("宽度(mm)")
                          modelAutoInfo.body_height = config.get("高度(mm)")
                          modelAutoInfo.body_wheelbase = config.get("轴距(mm)")
                          modelAutoInfo.body_front_track = config.get("前轮距(mm)")
                          modelAutoInfo.body_rear_track = config.get("后轮距(mm)")
                          modelAutoInfo.body_min_ground_clearance = config.get("最小离地间隙(mm)")
                          modelAutoInfo.body_weight = config.get("整备质量(kg)")
                          modelAutoInfo.body_structure = config.get("车身结构")
                          modelAutoInfo.body_doors = config.get("车门数(个)")
                          modelAutoInfo.body_seats = config.get("座位数(个)")
                          modelAutoInfo.body_oil_capacity = config.get("油箱容积(L)")
                          modelAutoInfo.body_trunk_capacity = config.get("行李厢容积(L)")
                          // 发动机
                          modelAutoInfo.engine_model = config.get("发动机型号")
                          modelAutoInfo.engine_cc = config.get("排量(mL)")
                          modelAutoInfo.engine_intake = config.get("进气形式")
                          modelAutoInfo.engine_arrangement = config.get("气缸排列形式")
                          modelAutoInfo.engine_cylinders = config.get("气缸数(个)")
                          modelAutoInfo.engine_valves = config.get("每缸气门数(个)")
                          modelAutoInfo.engine_compression_ratio = config.get("压缩比")
                          modelAutoInfo.engine_valve_mechanism = config.get("配气机构")
                          modelAutoInfo.engine_cylinder_diameter = config.get("缸径(mm)")
                          modelAutoInfo.engine_stroke = config.get("行程(mm)")
                          modelAutoInfo.engine_max_hp = config.get("最大马力(Ps)")
                          modelAutoInfo.engine_max_power = config.get("最大功率(kW)")
                          modelAutoInfo.engine_max_power_speed = config.get("最大功率转速(rpm)")
                          modelAutoInfo.engine_max_torque = config.get("最大扭矩(N・m)")
                          modelAutoInfo.engine_max_torque_speed = config.get("最大扭矩转速(rpm)")
                          modelAutoInfo.engine_tech = config.get("发动机特有技术")
                          modelAutoInfo.engine_oil_type = config.get("燃料形式")
                          modelAutoInfo.engine_oil_number = config.get("燃油标号")
                          modelAutoInfo.engine_oil_supply = config.get("供油方式")
                          modelAutoInfo.engine_cylinder_head_material = config.get("缸盖材料")
                          modelAutoInfo.engine_cylinder_body_material = config.get("缸体材料")
                          modelAutoInfo.engine_rohs = config.get("环保标准")
                          // 变速箱
                          modelAutoInfo.gearbox_name = config.get("简称")
                          modelAutoInfo.gearbox_number = config.get("挡位个数")
                          modelAutoInfo.gearbox_type = config.get("变速箱类型")
                          // 底盘转向
                          modelAutoInfo.underpan_drive_type = config.get("驱动方式")
                          modelAutoInfo.underpan_4wd_type = config.get("四驱形式")
                          modelAutoInfo.underpan_diff_mechanism_structure = config.get("中央差速器结构")
                          modelAutoInfo.underpan_front_suspension_type = config.get("前悬架类型")
                          modelAutoInfo.underpan_rear_suspension_type = config.get("后悬架类型")
                          modelAutoInfo.underpan_assistance_type = config.get("助力类型")
                          modelAutoInfo.underpan_body_structure = config.get("车体结构")
                          // 制动
                          modelAutoInfo.braking_front_type = config.get("前制动器类型")
                          modelAutoInfo.braking_rear_type = config.get("后制动器类型")
                          modelAutoInfo.braking_hold_type = config.get("驻车制动类型")
                          // 车轮
                          modelAutoInfo.tire_front_model = config.get("前轮胎规格")
                          modelAutoInfo.tire_rear_model = config.get("后轮胎规格")
                          modelAutoInfo.tire_spare_model = config.get("备胎规格")
                          // 安全装备
                          modelAutoInfo.safe_srs_main = config.get("主/副驾驶座安全气囊")
                          modelAutoInfo.safe_srs_side = config.get("前/后排侧气囊")
                          modelAutoInfo.safe_srs_head = config.get("前/后排头部气囊(气帘)")
                          modelAutoInfo.safe_srs_knee = config.get("膝部气囊")
                          modelAutoInfo.safe_tpms = config.get("胎压监测装置")
                          modelAutoInfo.safe_flat_free_tire = config.get("零胎压继续行驶")
                          modelAutoInfo.safe_belt_prompt = config.get("安全带未系提示")
                          modelAutoInfo.safe_isofix = config.get("ISOFIX儿童座椅接口")
                          modelAutoInfo.safe_engine_against_theft = config.get("发动机电子防盗")
                          modelAutoInfo.safe_central_lock = config.get("车内中控锁")
                          modelAutoInfo.safe_remote_ley = config.get("遥控钥匙")
                          modelAutoInfo.safe_keyless_go = config.get("无钥匙启动系统")
                          modelAutoInfo.safe_keyless_enter = config.get("无钥匙进入系统")
                          // 操控配置
                          modelAutoInfo.control_abs = config.get("ABS防抱死")
                          modelAutoInfo.control_cbc = config.get("制动力分配(EBD/CBC等)")
                          modelAutoInfo.control_bas = config.get("刹车辅助(EBA/BAS/BA等)")
                          modelAutoInfo.control_tsc = config.get("牵引力控制(ASR/TCS/TRC等)")
                          modelAutoInfo.control_esp = config.get("车身稳定控制(ESC/ESP/DSC等)")
                          modelAutoInfo.control_hac = config.get("上坡辅助")
                          modelAutoInfo.control_auto_hold = config.get("自动驻车")
                          modelAutoInfo.control_hdc = config.get("陡坡缓降")
                          modelAutoInfo.control_variable_suspension = config.get("可变悬架")
                          modelAutoInfo.control_air_suspension = config.get("空气悬架")
                          modelAutoInfo.control_variable_turning_rate = config.get("可变转向比")
                          modelAutoInfo.control_front_diff_mechanism = config.get("前桥限滑差速器/差速锁")
                          modelAutoInfo.control_central_diff_mechanism = config.get("中央差速器锁止功能")
                          modelAutoInfo.control_rear_diff_mechanism = config.get("后桥限滑差速器/差速锁")
                          // 外部配置
                          modelAutoInfo.out_electric_skylight = config.get("电动天窗")
                          modelAutoInfo.out_panoramic_skylight = config.get("全景天窗")
                          modelAutoInfo.out_sport_package = config.get("运动外观套件")
                          modelAutoInfo.out_alloy_wheel = config.get("铝合金轮圈")
                          modelAutoInfo.out_electric_close_door = config.get("电动吸合门")
                          modelAutoInfo.out_sideslip_door = config.get("侧滑门")
                          modelAutoInfo.out_electric_trunk = config.get("电动后备厢")
                          modelAutoInfo.out_response_trunk = config.get("感应后备厢")
                          modelAutoInfo.out_luggage_rack = config.get("车顶行李架")
                          // 内部配置
                          modelAutoInfo.inner_dermis_steering_wheel = config.get("真皮方向盘")
                          modelAutoInfo.inner_steering_wheel_adjust = config.get("方向盘调节")
                          modelAutoInfo.inner_steering_wheel_electric_adjust = config.get("方向盘电动调节")
                          modelAutoInfo.inner_multi_func_steering_wheel = config.get("多功能方向盘")
                          modelAutoInfo.inner_steering_wheel_shift = config.get("方向盘换挡")
                          modelAutoInfo.inner_steering_wheel_heating = config.get("方向盘加热")
                          modelAutoInfo.inner_steering_wheel_memory = config.get("方向盘记忆")
                          modelAutoInfo.inner_ccs = config.get("定速巡航")
                          modelAutoInfo.inner_hold_radar = config.get("前/后驻车雷达")
                          modelAutoInfo.inner_back_up_camera = config.get("倒车视频影像")
                          modelAutoInfo.inner_computer_display_screen = config.get("行车电脑显示屏")
                          modelAutoInfo.inner_lcd_dashboard = config.get("全液晶仪表盘")
                          modelAutoInfo.inner_hub = config.get("HUD抬头数字显示")
                          // 座椅配置
                          modelAutoInfo.seat_material = config.get("座椅材质")
                          modelAutoInfo.seat_sports_style = config.get("运动风格座椅")
                          modelAutoInfo.seat_high_adjust = config.get("座椅高低调节")
                          modelAutoInfo.seat_waist_adjust = config.get("腰部支撑调节")
                          modelAutoInfo.seat_shoulder_adjust = config.get("肩部支撑调节")
                          modelAutoInfo.seat_electric_adjust = config.get("主/副驾驶座电动调节")
                          modelAutoInfo.seat_sec_back_adjust = config.get("第二排靠背角度调节")
                          modelAutoInfo.seat_sec_move = config.get("第二排座椅移动")
                          modelAutoInfo.seat_sec_electric_adjust = config.get("后排座椅电动调节")
                          modelAutoInfo.seat_memory = config.get("电动座椅记忆")
                          modelAutoInfo.seat_heating = config.get("前/后排座椅加热")
                          modelAutoInfo.seat_air = config.get("前/后排座椅通风")
                          modelAutoInfo.seat_massage = config.get("前/后排座椅按摩")
                          modelAutoInfo.seat_third_row = config.get("第三排座椅")
                          modelAutoInfo.seat_fold_type = config.get("后排座椅放倒方式")
                          modelAutoInfo.seat_centre_armrest = config.get("前/后中央扶手")
                          modelAutoInfo.seat_rear_cup_shelf = config.get("后排杯架")
                          // 多媒体配置
                          modelAutoInfo.media_gps = config.get("GPS导航系统")
                          modelAutoInfo.media_location_serv = config.get("定位互动服务")
                          modelAutoInfo.media_colour_lcd = config.get("中控台彩色大屏")
                          modelAutoInfo.media_bluetooth_phone = config.get("蓝牙/车载电话")
                          modelAutoInfo.media_tv = config.get("车载电视")
                          modelAutoInfo.media_rear_lcd = config.get("后排液晶屏")
                          modelAutoInfo.media_power = config.get("220V/230V电源")
                          modelAutoInfo.media_aux_usb = config.get("外接音源接口")
                          modelAutoInfo.media_cd_mp3_support = config.get("CD支持MP3/WMA")
                          modelAutoInfo.media_cd_dvd_type = config.get("多媒体系统")
                          modelAutoInfo.media_sound_brand = config.get("扬声器品牌")
                          modelAutoInfo.media_loudspeaker_number = config.get("扬声器数量")
                          // 灯光配置
                          modelAutoInfo.light_low_beam_type = config.get("近光灯")
                          modelAutoInfo.light_high_beam_type = config.get("远光灯")
                          modelAutoInfo.light_drl = config.get("日间行车灯")
                          modelAutoInfo.light_adaptive_beam = config.get("自适应远近光")
                          modelAutoInfo.light_auto_beam = config.get("自动头灯")
                          modelAutoInfo.light_turn_auxiliary_lamp = config.get("转向辅助灯")
                          modelAutoInfo.light_turn_headlight = config.get("转向头灯")
                          modelAutoInfo.light_fog_light = config.get("前雾灯")
                          modelAutoInfo.light_high_adjust = config.get("大灯高度可调")
                          modelAutoInfo.light_clean = config.get("大灯清洗装置")
                          modelAutoInfo.light_atmosphere = config.get("车内氛围灯")
                          // 玻璃/后视镜
                          modelAutoInfo.glass_window_electric = config.get("前/后电动车窗")
                          modelAutoInfo.glass_window_anti_pinch = config.get("车窗防夹手功能")
                          modelAutoInfo.glass_anti_uv = config.get("防紫外线/隔热玻璃")
                          modelAutoInfo.glass_rearview_electric = config.get("后视镜电动调节")
                          modelAutoInfo.glass_rearview_heating = config.get("后视镜加热")
                          modelAutoInfo.glass_rearview_anti_dazzling = config.get("内/外后视镜自动防眩目")
                          modelAutoInfo.glass_rearview_auto_fold = config.get("后视镜电动折叠")
                          modelAutoInfo.glass_rearview_memory = config.get("后视镜记忆")
                          modelAutoInfo.glass_rear_window_sunshade = config.get("后风挡遮阳帘")
                          modelAutoInfo.glass_rear_side_window_sunshade = config.get("后排侧遮阳帘")
                          modelAutoInfo.glass_rear_privacy = config.get("后排侧隐私玻璃")
                          modelAutoInfo.glass_cosmetic_mirror = config.get("遮阳板化妆镜")
                          modelAutoInfo.glass_rear_wiper = config.get("后雨刷")
                          modelAutoInfo.glass_response_wiper = config.get("感应雨刷")
                          // 空调/冰箱
                          modelAutoInfo.air_cond_type = config.get("空调控制方式")
                          modelAutoInfo.air_cond_rear_independent = config.get("后排独立空调")
                          modelAutoInfo.air_cond_rear_port = config.get("后座出风口")
                          modelAutoInfo.air_cond_zoning = config.get("温度分区控制")
                          modelAutoInfo.air_cond_air_adjust = config.get("车内空气调节/花粉过滤")
                          modelAutoInfo.fridge = config.get("车载冰箱")
                          // 高科技配置
                          modelAutoInfo.tech_park_assist = config.get("自动泊车入位")
                          modelAutoInfo.tech_engine_start_stop = config.get("发动机启停技术")
                          modelAutoInfo.tech_blindness_alert = config.get("并线辅助")
                          modelAutoInfo.tech_lane_deviate_alert = config.get("车道偏离预警系统")
                          modelAutoInfo.tech_auto_brake = config.get("主动刹车/主动安全系统")
                          modelAutoInfo.tech_active_steering = config.get("整体主动转向系统")
                          modelAutoInfo.tech_night_vision = config.get("夜视系统")
                          modelAutoInfo.tech_splitview = config.get("中控液晶屏分屏显示")
                          modelAutoInfo.tech_acc = config.get("自适应巡航")
                          modelAutoInfo.tech_pancam = config.get("全景摄像头")
                        } catch {
                          case e: Throwable =>
                            logger.error("Process error.", e)
                        }
                        modelAutoInfo
                    }
                }.toList
                MdsWorkerBasicContext.dataExchangeWorker.insert(seriesObjects)
            }
        }
    }
    Resp.success(null)
  }

  private def parseConfig(detail: String): List[ConfigOpt] = {
    val configLineOpt = detail.split("\r\n").find(_.trim.startsWith("var config ="))
    if (configLineOpt.isEmpty) {
      // 此车系没有配置信息
      List()
    } else {
      val configLine = configLineOpt.get.trim
      val config = JsonHelper.toJson(configLine.substring("var config =".length)).get("result").get("paramtypeitems")
      val configOptLine = detail.split("\r\n").find(_.trim.startsWith("var option =")).get.trim
      val configOpt = JsonHelper.toJson(configOptLine.substring("var option =".length)).get("result").get("configtypeitems")

      val modelSize = config.head.get("paramitems").head.get("valueitems").size()
      val result = for (i <- 0 until modelSize) yield {
        val configMap = config.flatMap {
          _.get("paramitems").map {
            item =>
              val value = item.get("valueitems").get(i).get("value").asText() match {
                case "-" => ""
                case v => v
              }
              item.get("name").asText() -> value
          }
        }.toMap ++
          configOpt.flatMap {
            _.get("configitems").map {
              item =>
                val value = item.get("valueitems").get(i).get("value").asText() match {
                  case v if v.contains("●") => "Y"
                  case v if v.contains("○") => "O"
                  case "-" => "N"
                  // TODO like 前● / 后●
                  case v => v
                }
                item.get("name").asText() -> value
            }
          }.toMap
        ConfigOpt(configMap)
      }
      result.toList
    }
  }


  implicit def strToFloat(str: String): Float = {
    new java.lang.Float(formatStrToNumber(str))
  }

  implicit def strToLong(str: String): Long = {
    new java.lang.Long(formatStrToNumber(str))
  }

  def formatStrToNumber(str: String): String = {
    str.trim match {
      case "" => "0"
      case "未知" => "0"
      case "暂无报价" => "0"
      case s if s.contains("-") => s.split("-")(1)
      case s if s.contains("~") => s.split("~")(1)
      case s => s
    }
  }

  case class ConfigOpt(config: Map[String, String]) {
    def get(key: String): String = config.getOrElse(key, "")
  }

}