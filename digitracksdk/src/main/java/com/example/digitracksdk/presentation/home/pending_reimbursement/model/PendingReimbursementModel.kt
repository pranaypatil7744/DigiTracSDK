package com.example.digitracksdk.presentation.home.pending_reimbursement.model

import androidx.annotation.Keep

@Keep
data class PendingReimbursementModel(
    var title:String? = "",
    var date:String? = "",
    var amount:String? = "",
    var isSelected:Boolean = false,
    var icon:Int? = null,
    var AR_AssociateReimbursementId:String? ="",
    var month:String? ="",
    var year:String? ="",
    var inTime:String?="",
    var outTime:String?=""
)
