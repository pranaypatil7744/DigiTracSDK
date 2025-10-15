package com.example.digitracksdk.domain.model.home_model.response

import androidx.annotation.Keep

@Keep
data class CheckBirthdayResponseModel(
    var IsBirthday:Int? = 0,
    var ImagePath:String? = "",
    var BdayImageArr:String? = "",
    var ColorCode:String? = "",
    var BirthdayMessage:String? = "",
    var status:String? = "",
    var Message:String? = ""
)
