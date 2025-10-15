package com.example.digitracksdk.presentation.pf_esic_insurance.model

import androidx.annotation.Keep

@Keep
data class PfEsicResponseModel(
    var ImageArr:String? = "",
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class PfEsicRequestModel(
    var GNETAssociateID:String = ""
)
