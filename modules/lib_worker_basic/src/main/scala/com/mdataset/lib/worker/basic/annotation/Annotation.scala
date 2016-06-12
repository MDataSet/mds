package com.mdataset.lib.worker.basic.annotation

import scala.annotation.StaticAnnotation

/**
  * 实体注解
  *
  * 所有需要持久化的数据实体都要应用此注解
  */
case class BDEntity(desc: String = "") extends StaticAnnotation

/**
  * 列族注解
  *
  * @param name 列族名称
  */
case class Family(name: String) extends StaticAnnotation

