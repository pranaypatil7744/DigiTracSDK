package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class AttendanceTimeSheetResponseModel(
    var Associate:String?= "",
    var StartDate:String?= "",
    var EndDate:String?= "",
    var AttendenceWeekList:ArrayList<AttendenceWeekListModel>?= ArrayList(),
    var Status:String?= "",
    var Message:String?= ""
)

@Keep
data class AttendenceWeekListModel(
    var WeekNo:Int? =0,
    var AttendenceList:ArrayList<AttendenceListModel>? = ArrayList()
)

@Keep
data class AttendenceListModel(
    var WeekStartDate:String? ="",
    var WeekEndDate:String? ="",
    var AttendanceDate:String? ="",
    var Workinghrs:String? ="",
    var InDateTime:String? ="",
    var OutDateTime:String? ="",
    var AttendenceStatus:String? ="",
)

@Keep
data class AttendanceTimeSheetRequestModel(
    var InnovID:String ="",
    var Startdate:String ="",
    var Enddate:String ="",
)
