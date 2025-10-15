package com.example.digitracksdk.domain.model.onboarding.pf_uan

import androidx.annotation.Keep


/**
 * Created by Mo Khurseed Ansari on 18-10-2023.
 */
@Keep
data class GetPFESICResponseModel(
    val InnovId: String? = "",
    val IsPFUAN: String? = "",
    val PFUANNumber: String? = "",
    val IsESICIp: String? = "",
    val IsESICIpNumber: String? = "",
    val status: String? = "",
    val Message: String? = ""
)

@Keep
data class GetPFESICRequestModel(
    var InnovId: String? = ""
)
