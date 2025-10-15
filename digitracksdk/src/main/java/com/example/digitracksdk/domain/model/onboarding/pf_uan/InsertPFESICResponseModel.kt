package com.example.digitracksdk.domain.model.onboarding.pf_uan

import androidx.annotation.Keep


/**
 * Created by Mo Khurseed Ansari on 18-10-2023.
 */
@Keep
data class InsertPFESICResponseModel(
    val status: String? = "",
    val Message: String? = ""
)

@Keep
data class InsertPFESICRequestModel(
    var InnovId: String? = "",
    var IsESICIp: String? = "",
    var IsESICIpNumber: String? = "",
    var IsPFUAN: String? = "",
    var PFUANNumber: String? = "",

    )
