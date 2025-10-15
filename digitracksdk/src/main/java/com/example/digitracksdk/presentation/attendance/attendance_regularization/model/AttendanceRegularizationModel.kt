package com.example.digitracksdk.presentation.attendance.attendance_regularization.model

import androidx.annotation.Keep
import com.example.digitracksdk.presentation.leaves.apply_leave.model.LeavesStatus

@Keep
data class AttendanceRegularizationModel(
    var requestMsg:String? = "",
    var requestType:String? = "",
    var requestDate:String? = "",
    var requestStatus: LeavesStatus,
    var responseDate:String? = ""
)
