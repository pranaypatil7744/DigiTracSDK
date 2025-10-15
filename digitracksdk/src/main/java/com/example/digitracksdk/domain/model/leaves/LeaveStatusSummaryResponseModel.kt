package com.example.digitracksdk.domain.model.leaves

import androidx.annotation.Keep

@Keep
data class LeaveStatusSummaryResponseModel(
    var AssociateID:String? ="",
    var TotalLeaveRequest:String? ="",
    var LeavesApproved:String? ="",
    var PendingApproval:String? ="",
    var LeavesRejected:String? ="",
    var Status:String? ="",
    var Message:String? ="",
)
