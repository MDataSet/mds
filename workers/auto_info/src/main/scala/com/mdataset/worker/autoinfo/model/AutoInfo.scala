package com.mdataset.worker.autoinfo.model

import com.mdataset.lib.core.model.MdsIdModel

import scala.beans.BeanProperty

/*class AutoInfoDTO extends IdModel {
  @BeanProperty var brand_name: String = _
  @BeanProperty var brand_image: String = _
  @BeanProperty var company_auto_info: List[CompanyAutoInfoDTO] = _
}

class CompanyAutoInfoDTO {
  @BeanProperty var company_name: String = _
  @BeanProperty var series_auto_info: List[SeriesAutoInfoDTO] = _

}

class SeriesAutoInfoDTO {
  @BeanProperty var series_name: String = _
}*/

class ModelAutoInfo extends MdsIdModel {
  @BeanProperty var brand_name: String = _
  @BeanProperty var company_name: String = _
  @BeanProperty var series_name: String = _
  @BeanProperty var model_name: String = _
  @BeanProperty var company_price_min: Float = _
  @BeanProperty var dealer_price_max: Float = _
  @BeanProperty var rating: Float = _

  // 级别
  @BeanProperty var basic_category: String = _
  // 发动机
  @BeanProperty var basic_engine: String = _
  // 变速箱
  @BeanProperty var basic_transmission_box: String = _
  // 长*宽*高(mm)
  @BeanProperty var basic_volume: String = _
  // 车身结构
  @BeanProperty var basic_structure: String = _
  // 最高车速(km/h)
  @BeanProperty var basic_max_speed: Float = _
  // 官方0-100km/h加速(s)
  @BeanProperty var basic_official_acceleration: Float = _
  // 实测0-100km/h加速(s)
  @BeanProperty var basic_real_acceleration: Float = _
  // 实测100-0km/h制动(m)
  @BeanProperty var basic_real_brake: Float = _
  // 实测油耗(L/100km)
  @BeanProperty var basic_real_oil_consumption: Float = _
  // 工信部综合油耗(L/100km)
  @BeanProperty var basic_official_oil_consumption: Float = _
  // 实测离地间隙(mm)
  @BeanProperty var basic_ground_clearance: Float = _
  // 整车质保
  @BeanProperty var basic_warranty: String = _

  // 长度(mm)
  @BeanProperty var body_length: Long = _
  // 宽度(mm)
  @BeanProperty var body_width: Long = _
  // 高度(mm)
  @BeanProperty var body_height: Long = _
  // 轴距(mm)
  @BeanProperty var body_wheelbase: Long = _
  // 前轮距(mm)
  @BeanProperty var body_front_track: Long = _
  // 后轮距(mm)
  @BeanProperty var body_rear_track: Long = _
  // 最小离地间隙(mm)
  @BeanProperty var body_min_ground_clearance: Long = _
  // 整备质量(kg)
  @BeanProperty var body_weight: Long = _
  // 车身结构
  @BeanProperty var body_structure: String = _
  // 车门数(个)
  @BeanProperty var body_doors: Long = _
  // 座位数(个)
  @BeanProperty var body_seats: Long = _
  // 油箱容积(L)
  @BeanProperty var body_oil_capacity: Long = _
  // 行李厢容积(L)
  @BeanProperty var body_trunk_capacity: String = _

  // 发动机型号
  @BeanProperty var engine_model: String = _
  // 排量(mL)
  @BeanProperty var engine_cc: Long = _
  // 进气形式
  @BeanProperty var engine_intake: String = _
  // 气缸排列形式
  @BeanProperty var engine_arrangement: String = _
  // 气缸数(个)
  @BeanProperty var engine_cylinders: Long = _
  // 每缸气门数(个)
  @BeanProperty var engine_valves: Long = _
  // 压缩比
  @BeanProperty var engine_compression_ratio: Float = _
  // 配气机构
  @BeanProperty var engine_valve_mechanism: String = _
  // 缸径(mm)
  @BeanProperty var engine_cylinder_diameter: Float = _
  // 行程(mm)
  @BeanProperty var engine_stroke: Float = _
  // 最大马力(Ps)
  @BeanProperty var engine_max_hp: Long = _
  // 最大功率(kW)
  @BeanProperty var engine_max_power: Long = _
  // 最大功率转速(rpm)
  @BeanProperty var engine_max_power_speed: Long = _
  // 最大扭矩(N·m)
  @BeanProperty var engine_max_torque: Long = _
  // 最大扭矩转速(rpm)
  @BeanProperty var engine_max_torque_speed: String = _
  // 发动机特有技术
  @BeanProperty var engine_tech: String = _
  // 燃料形式
  @BeanProperty var engine_oil_type: String = _
  // 燃油标号
  @BeanProperty var engine_oil_number: String = _
  // 供油方式
  @BeanProperty var engine_oil_supply: String = _
  // 缸盖材料
  @BeanProperty var engine_cylinder_head_material: String = _
  // 缸体材料
  @BeanProperty var engine_cylinder_body_material: String = _
  // 环保标准
  @BeanProperty var engine_rohs: String = _

  // 变速箱
  @BeanProperty var gearbox_name: String = _
  // 挡位个数
  @BeanProperty var gearbox_number: Long = _
  // 变速箱类型
  @BeanProperty var gearbox_type: String = _

  // 驱动方式
  @BeanProperty var underpan_drive_type: String = _
  // 四驱形式
  @BeanProperty var underpan_4wd_type: String = _
  // 中央差速器结构
  @BeanProperty var underpan_diff_mechanism_structure: String = _
  // 前悬架类型
  @BeanProperty var underpan_front_suspension_type: String = _
  // 后悬架类型
  @BeanProperty var underpan_rear_suspension_type: String = _
  // 助力类型
  @BeanProperty var underpan_assistance_type: String = _
  // 车体结构
  @BeanProperty var underpan_body_structure: String = _

  // 前制动器类型
  @BeanProperty var braking_front_type: String = _
  // 后制动器类型
  @BeanProperty var braking_rear_type: String = _
  // 驻车制动类型
  @BeanProperty var braking_hold_type: String = _

  // 前轮胎规格
  @BeanProperty var tire_front_model: String = _
  // 后轮胎规格
  @BeanProperty var tire_rear_model: String = _
  // 备胎规格
  @BeanProperty var tire_spare_model: String = _

  // 主/副驾驶座安全气囊
  @BeanProperty var safe_srs_main: String = _
  // 前/后排侧气囊
  @BeanProperty var safe_srs_side: String = _
  // 前/后排头部气囊(气帘)
  @BeanProperty var safe_srs_head: String = _
  // 膝部气囊
  @BeanProperty var safe_srs_knee: String = _
  // 胎压监测装置
  @BeanProperty var safe_tpms: String = _
  // 零胎压继续行驶
  @BeanProperty var safe_flat_free_tire: String = _
  // 安全带未系提示
  @BeanProperty var safe_belt_prompt: String = _
  // ISOFIX儿童座椅接口
  @BeanProperty var safe_isofix: String = _
  // 发动机电子防盗
  @BeanProperty var safe_engine_against_theft: String = _
  // 车内中控锁
  @BeanProperty var safe_central_lock: String = _
  // 遥控钥匙
  @BeanProperty var safe_remote_ley: String = _
  // 无钥匙启动系统
  @BeanProperty var safe_keyless_go: String = _
  // 无钥匙进入系统
  @BeanProperty var safe_keyless_enter: String = _

  // ABS防抱死
  @BeanProperty var control_abs: String = _
  // 制动力分配(EBD/CBC等)
  @BeanProperty var control_cbc: String = _
  // 刹车辅助(EBA/BAS/BA等)
  @BeanProperty var control_bas: String = _
  // 牵引力控制(ASR/TCS/TRC等)
  @BeanProperty var control_tsc: String = _
  // 车身稳定控制(ESC/ESP/DSC等)
  @BeanProperty var control_esp: String = _
  // 上坡辅助
  @BeanProperty var control_hac: String = _
  // 自动驻车
  @BeanProperty var control_auto_hold: String = _
  // 陡坡缓降
  @BeanProperty var control_hdc: String = _
  // 可变悬架
  @BeanProperty var control_variable_suspension: String = _
  // 空气悬架
  @BeanProperty var control_air_suspension: String = _
  // 可变转向比
  @BeanProperty var control_variable_turning_rate: String = _
  // 前桥限滑差速器/差速锁
  @BeanProperty var control_front_diff_mechanism: String = _
  // 中央差速器锁止功能
  @BeanProperty var control_central_diff_mechanism: String = _
  // 后桥限滑差速器/差速锁
  @BeanProperty var control_rear_diff_mechanism: String = _

  // 电动天窗
  @BeanProperty var out_electric_skylight: String = _
  // 全景天窗
  @BeanProperty var out_panoramic_skylight: String = _
  // 运动外观套件
  @BeanProperty var out_sport_package: String = _
  // 铝合金轮圈
  @BeanProperty var out_alloy_wheel: String = _
  // 电动吸合门
  @BeanProperty var out_electric_close_door: String = _
  // 侧滑门
  @BeanProperty var out_sideslip_door: String = _
  // 电动后备厢
  @BeanProperty var out_electric_trunk: String = _
  // 感应后备厢
  @BeanProperty var out_response_trunk: String = _
  // 车顶行李架
  @BeanProperty var out_luggage_rack: String = _

  // 真皮方向盘
  @BeanProperty var inner_dermis_steering_wheel: String = _
  // 方向盘调节
  @BeanProperty var inner_steering_wheel_adjust: String = _
  // 方向盘电动调节
  @BeanProperty var inner_steering_wheel_electric_adjust: String = _
  // 多功能方向盘
  @BeanProperty var inner_multi_func_steering_wheel: String = _
  // 方向盘换挡
  @BeanProperty var inner_steering_wheel_shift: String = _
  // 方向盘加热
  @BeanProperty var inner_steering_wheel_heating: String = _
  // 方向盘记忆
  @BeanProperty var inner_steering_wheel_memory: String = _
  // 定速巡航
  @BeanProperty var inner_ccs: String = _
  // 前/后驻车雷达
  @BeanProperty var inner_hold_radar: String = _
  // 倒车视频影像
  @BeanProperty var inner_back_up_camera: String = _
  // 行车电脑显示屏
  @BeanProperty var inner_computer_display_screen: String = _
  // 全液晶仪表盘
  @BeanProperty var inner_lcd_dashboard: String = _
  // HUD抬头数字显示
  @BeanProperty var inner_hub: String = _

  // 座椅材质
  @BeanProperty var seat_material: String = _
  // 运动风格座椅
  @BeanProperty var seat_sports_style: String = _
  // 座椅高低调节
  @BeanProperty var seat_high_adjust: String = _
  // 腰部支撑调节
  @BeanProperty var seat_waist_adjust: String = _
  // 肩部支撑调节
  @BeanProperty var seat_shoulder_adjust: String = _
  // 主/副驾驶座电动调节
  @BeanProperty var seat_electric_adjust: String = _
  // 第二排靠背角度调节
  @BeanProperty var seat_sec_back_adjust: String = _
  // 第二排座椅移动
  @BeanProperty var seat_sec_move: String = _
  // 后排座椅电动调节
  @BeanProperty var seat_sec_electric_adjust: String = _
  // 电动座椅记忆
  @BeanProperty var seat_memory: String = _
  // 前/后排座椅加热
  @BeanProperty var seat_heating: String = _
  // 前/后排座椅通风
  @BeanProperty var seat_air: String = _
  // 前/后排座椅按摩
  @BeanProperty var seat_massage: String = _
  // 第三排座椅
  @BeanProperty var seat_third_row: String = _
  // 后排座椅放倒方式
  @BeanProperty var seat_fold_type: String = _
  // 前/后中央扶手
  @BeanProperty var seat_centre_armrest: String = _
  // 后排杯架
  @BeanProperty var seat_rear_cup_shelf: String = _

  // GPS导航系统
  @BeanProperty var media_gps: String = _
  // 定位互动服务
  @BeanProperty var media_location_serv: String = _
  // 中控台彩色大屏
  @BeanProperty var media_colour_lcd: String = _
  // 蓝牙/车载电话
  @BeanProperty var media_bluetooth_phone: String = _
  // 车载电视
  @BeanProperty var media_tv: String = _
  // 后排液晶屏
  @BeanProperty var media_rear_lcd: String = _
  // 220V/230V电源
  @BeanProperty var media_power: String = _
  // 外接音源接口
  @BeanProperty var media_aux_usb: String = _
  // CD支持MP3/WMA
  @BeanProperty var media_cd_mp3_support: String = _
  // 多媒体系统
  @BeanProperty var media_cd_dvd_type: String = _
  // 扬声器品牌
  @BeanProperty var media_sound_brand: String = _
  // 扬声器数量
  @BeanProperty var media_loudspeaker_number: String = _

  // 近光灯类型
  @BeanProperty var light_low_beam_type: String = _
  // 远光灯类型
  @BeanProperty var light_high_beam_type: String = _
  // 日间行车灯
  @BeanProperty var light_drl: String = _
  // 自适应远近光
  @BeanProperty var light_adaptive_beam: String = _
  // 自动头灯
  @BeanProperty var light_auto_beam: String = _
  // 转向辅助灯
  @BeanProperty var light_turn_auxiliary_lamp: String = _
  // 转向头灯
  @BeanProperty var light_turn_headlight: String = _
  // 前雾灯
  @BeanProperty var light_fog_light: String = _
  // 大灯高度可调
  @BeanProperty var light_high_adjust: String = _
  // 大灯清洗装置
  @BeanProperty var light_clean: String = _
  // 车内氛围灯
  @BeanProperty var light_atmosphere: String = _

  // 前/后电动车窗
  @BeanProperty var glass_window_electric: String = _
  // 车窗防夹手功能
  @BeanProperty var glass_window_anti_pinch: String = _
  // 防紫外线/隔热玻璃
  @BeanProperty var glass_anti_uv: String = _
  // 后视镜电动调节
  @BeanProperty var glass_rearview_electric: String = _
  // 后视镜加热
  @BeanProperty var glass_rearview_heating: String = _
  // 内/外后视镜自动防眩目
  @BeanProperty var glass_rearview_anti_dazzling: String = _
  // 后视镜电动折叠
  @BeanProperty var glass_rearview_auto_fold: String = _
  // 后视镜记忆
  @BeanProperty var glass_rearview_memory: String = _
  // 后风挡遮阳帘
  @BeanProperty var glass_rear_window_sunshade: String = _
  // 后排侧遮阳帘
  @BeanProperty var glass_rear_side_window_sunshade: String = _
  // 后排侧隐私玻璃
  @BeanProperty var glass_rear_privacy: String = _
  // 遮阳板化妆镜
  @BeanProperty var glass_cosmetic_mirror: String = _
  // 后雨刷
  @BeanProperty var glass_rear_wiper: String = _
  // 感应雨刷
  @BeanProperty var glass_response_wiper: String = _

  // 空调控制方式
  @BeanProperty var air_cond_type: String = _
  // 后排独立空调
  @BeanProperty var air_cond_rear_independent: String = _
  // 后座出风口
  @BeanProperty var air_cond_rear_port: String = _
  // 温度分区控制
  @BeanProperty var air_cond_zoning: String = _
  // 车内空气调节/花粉过滤
  @BeanProperty var air_cond_air_adjust: String = _

  // 车载冰箱
  @BeanProperty var fridge: String = _

  // 自动泊车入位
  @BeanProperty var tech_park_assist: String = _
  // 发动机启停技术
  @BeanProperty var tech_engine_start_stop: String = _
  // 并线辅助
  @BeanProperty var tech_blindness_alert: String = _
  // 车道偏离预警系统
  @BeanProperty var tech_lane_deviate_alert: String = _
  // 主动刹车/主动安全系统
  @BeanProperty var tech_auto_brake: String = _
  // 整体主动转向系统
  @BeanProperty var tech_active_steering: String = _
  // 夜视系统
  @BeanProperty var tech_night_vision: String = _
  // 中控液晶屏分屏显示
  @BeanProperty var tech_splitview: String = _
  // 自适应巡航
  @BeanProperty var tech_acc: String = _
  // 全景摄像头
  @BeanProperty var tech_pancam: String = _


}




