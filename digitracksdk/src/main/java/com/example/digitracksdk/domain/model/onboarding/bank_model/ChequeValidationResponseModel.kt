package com.example.digitracksdk.domain.model.onboarding.bank_model

import androidx.annotation.Keep

@Keep
data class ChequeValidationResponseModel(
    var status:String? ="",
    var Message:String? ="",
    var IsChequeUploaded:Boolean? =false,
    var Image:String? ="",
    var Extn:String? ="",
)

