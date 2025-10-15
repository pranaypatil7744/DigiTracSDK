package com.example.digitracksdk.domain.model.help_and_support

import androidx.annotation.Keep

@Keep
data class IssueCategoryResponseModel(
    var IssueCategoryID:Int? = 0,
    var IssueCategoryName:String? = "",
    var Message:String? = "",
)

@Keep
data class IssueCategoryRequestModel(
    var InnovID:String =""
)