package com.example.digitracksdk.domain.model.leaves

import androidx.annotation.Keep

@Keep
data class HolidaysListResponseModel(
    var LstHolidays:ArrayList<ListHolidaysModel>? = ArrayList(),
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class ListHolidaysModel(
    var HolidayName:String? ="",
    var HolidayDate:String? =""
)
