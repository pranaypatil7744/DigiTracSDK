package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class DashboardAttendanceStatusResponseModel(
    var AttendanceDateCollection:ArrayList<AttendanceDateCollectionModel>? = ArrayList(),
    var LstAttendanceDropDown:ArrayList<String>?=ArrayList(),
    var AssociateID:String?= "",
    var InnovID:String?= "",
    var InorOutFlag:String?= "",
    var InTime:String?= "",
    var OutTime:String?= "",
    var Picture:String?= "",
    var Tolworkinghrs:String?= "",
    var WorkingLocation:String?= "",
    var WorkingLocationID:String?= "",
    var CurrentServerTime:String?= "",
    var AttendanceAbbreviationType:String?= "",
    var AttendanceDate:String?= "",
    var IsDayorNight:String?= "",
    var Status:String?= "",
    var Message:String?= ""
)

@Keep
data class AttendanceDateCollectionModel(
    var AttendanceDate:String? ="",
    var IsPreviousDay:String? ="",
)
