package com.example.digitracksdk.domain.model.notification

import androidx.annotation.Keep

@Keep
data class NotificationDetailsResponseModel(
    var NotificationImageArr:String? = "",
    var NotificationFilePath:String? = "",
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class NotificationDetailsRequestModel(
    var NotificationId:Int? = 0
)
