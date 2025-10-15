package com.example.digitracksdk.presentation.onboarding.kyc_screen.model

import androidx.annotation.Keep

@Keep
data class KycModel(
    var documentName:String? = "",
    var documentUrl:String? = "",
    var fileName:String? = ""
)
