package com.example.digitracksdk.presentation.attendance.model

import androidx.annotation.Keep
import com.example.digitracksdk.domain.model.attendance_model.AttendenceListModel

@Keep
data class AttendanceTimeSheetModel(
    var monthYear :String? = "",
    var thisMonthHistory :ArrayList<AttendenceListModel>? = ArrayList(),
    var timeSheetItemType: TimeSheetItemType
)
//data class SelectedMonthHistory(
//    var date :String? = "",
//    var dayOfWeek :String? = "",
//    var monthYear :String? = "",
//    var checkIn :String? = "",
//    var checkOut :String? = "",
//    var workingHrs :String? = "",
//    var holiday :String? = "",
//    var timeSheetItemType: TimeSheetItemType
//)

enum class TimeSheetItemType(val value:Int){
    TIME_SHEET_TOP_ITEM(1),
    TIME_SHEET_DAILY_ITEM(2),
    TIME_SHEET_HOLIDAY_ITEM(3)
}
