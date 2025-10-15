package com.example.digitracksdk.domain.model.login_model

import androidx.annotation.Keep

@Keep
data class FirebaseTokenUpdateResponseModel(
    var Status:String?="",
    var Message:String?="",
)

@Keep
data class FirebaseTokenUpdateRequestModel(
    var FirebaseToken:String ="",
    var InnovID:String =""
)
