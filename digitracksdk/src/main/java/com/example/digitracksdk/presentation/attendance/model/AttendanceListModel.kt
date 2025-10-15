package com.example.digitracksdk.presentation.attendance.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class AttendanceListModel(
    var attendanceStatus: AttendanceStatus = AttendanceStatus.NONE,
    var attendanceType: AttendanceType,
    var time:String? = "",
    var dateDay:String? = "",
    var checkInTime:String? = "",
    var checkOutTime:String? = "",
    var workingHrs:String? = "",
    var attendanceItemName:String? = "",
    var attendanceItemIcon: Int? = null,
    var isAttendanceAnywhere:Boolean? = false
):Serializable
enum class AttendanceStatus(val value:Int){
    CHECK_IN(1),
    CHECK_OUT(2),
    NONE(3)
}
enum class AttendanceType(val value: Int){
    ATTENDANCE_TOP_ITEM(10),
    ATTENDANCE_ITEM_LIST(20)
}
