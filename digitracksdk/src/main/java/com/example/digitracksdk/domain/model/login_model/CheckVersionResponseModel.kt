package com.example.digitracksdk.domain.model.login_model

import androidx.annotation.Keep

@Keep
data class CheckVersionResponseModel(
    var DigitracVersion:String? = "",
    var Status:String? = "",
    var Message:String? = "",
)
