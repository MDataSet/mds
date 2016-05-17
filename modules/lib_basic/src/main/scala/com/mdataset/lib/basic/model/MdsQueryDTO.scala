package com.mdataset.lib.basic.model

case class MdsQueryFormatDTO(
                          field: String,
                          default_value: String,
                          data_type: String,
                          desc: String,
                          necessary: Boolean
                        )

case class MdsQueryLimitDTO(
                         hourly_rate: Long
                       )