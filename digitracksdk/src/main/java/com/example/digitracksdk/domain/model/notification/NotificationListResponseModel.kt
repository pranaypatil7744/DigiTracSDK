package com.example.digitracksdk.domain.model.notification

import androidx.annotation.Keep

@Keep
data class NotificationListResponseModel(
    var lstMobileNotifications:ArrayList<ListNotificationsModel>?= ArrayList(),
    var status:String?="",
    var Message:String?="",
)

@Keep
data class ListNotificationsModel(
    var NotificationId:Int?=0,
    var Caption:String?="",
    var ClientName:String?="",
    var NotificationFileName:String?="",
    var UploadedOn:String?=""
)
