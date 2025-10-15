package com.example.digitracksdk.presentation.leaves.apply_leave.model

import androidx.annotation.Keep

@Keep
data class ApplyLeavesModel(
    var leaveTitle:String? = "",
    var leaveMsg:String? = "",
    var startDate:String? = "",
    var endDate:String? = "",
    var totalDays:String? = "",
    val myLeaveDashboardList: ArrayList<MyLeaveDashboardModel>? = ArrayList(),
    var leaveStatus: LeavesStatus? = null,
    var leavesDisplayType: LeavesType
)
@Keep
data class MyLeaveDashboardModel(
    var leaveMsg:String? = "",
    var remainingLeaves:String? = "",
    var leavesIcon:Int? =null,
)

enum class LeavesStatus(val value: Int){
    APPROVED(1),
    PENDING(2),
    REJECTED(3),
}
enum class LeavesType(val value: Int){
    MY_LEAVES(10),
    LEAVES_HISTORY(20),
    TITLE(30),
    LEAVE_STATUS(40)
}
