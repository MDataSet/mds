package com.mdataset.startup

import com.mdataset.lib.basic.model.MdsSourceDTO

object MdsContext {

  val sources: collection.mutable.Map[String, MdsSourceDTO] = collection.mutable.Map[String, MdsSourceDTO]()

}
