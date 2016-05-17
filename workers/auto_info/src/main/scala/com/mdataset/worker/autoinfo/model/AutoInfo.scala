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
  @BeanProperty var company_price: Float = _
  @BeanProperty var dealer_price: Float = _
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
  // TODO

}




