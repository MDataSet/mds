package com.mdataset.lib.worker.basic.annotation

import scala.annotation.StaticAnnotation

case class Entity(desc: String = "") extends StaticAnnotation

case class Family(name: String) extends StaticAnnotation

