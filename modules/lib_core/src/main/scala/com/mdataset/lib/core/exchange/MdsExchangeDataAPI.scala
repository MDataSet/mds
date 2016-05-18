package com.mdataset.lib.core.exchange

import com.ecfront.common.Resp

trait MdsExchangeDataAPI {

  def insert(itemCode: String, lines: List[Any]): Resp[Void]

}
