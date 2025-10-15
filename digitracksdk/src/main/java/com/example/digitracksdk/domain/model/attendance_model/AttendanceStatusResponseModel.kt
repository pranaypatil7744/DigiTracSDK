package com.example.digitracksdk.domain.model.attendance_model

import androidx.annotation.Keep

@Keep
data class AttendanceStatusResponseModel(
    var IsIN:String?="",
    var IsOUT:String?="",
    var Attendancedate:String?="",
    var IsTodaysAttendance:String?="",
    var Status:String?="",
    var Message:String?="",
)
