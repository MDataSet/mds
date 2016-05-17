package com.mdataset.lib.basic.model

case class MdsCollectLimitDTO(
                           per_second_max_times: Long,
                           per_minute_max_times: Long,
                           daily_max_times: Long,
                           monthly_max_times: Long
                         )

case class MdsCollectAuthDTO(
                          user_name: String,
                          password: String,
                          account_token: String,
                          ssh_key: String
                        )
