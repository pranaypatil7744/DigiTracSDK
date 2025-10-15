package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class ViewAttendanceResponseModel(
    val LSTAttendaceTimeAsheetDetails:ArrayList<ListViewAttendanceModel>? = ArrayList(),
    var Status:String? = "",
    var Message:String? = ""
)
@Keep
data class ListViewAttendanceModel(
    var AttendanceDate:String? = "",
    var AttendanceStatus:String? = "",
    val Colour: String?="",
    var Day:String? = "",
    val HexCode: String?="",
    var InsTime:String? = "",
    var OutTime:String? = "",
    var Shift:String? = "",
    var WorkHours:String? = "",
)

@Keep
data class ViewAttendanceRequestModel(
    var AssociateID:String? = "",
    var FromDate:String? = "",
    var MappingID:String? = null,
    var Todate:String? = "",
)
