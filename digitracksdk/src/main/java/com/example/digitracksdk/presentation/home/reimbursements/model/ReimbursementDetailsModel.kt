package com.example.digitracksdk.presentation.home.reimbursements.model

import androidx.annotation.Keep

@Keep
data class ReimburseDetailsModel(
    var reimbursementDetailsList: ArrayList<ReimbursementDetailsListModel>?= ArrayList()
)
@Keep
data class ReimbursementDetailsListModel(
    var title:String? = "",
    var subTitle:String? = "",
    var title2:String? = "",
    var subTitle2:String? = "",
    var associateId:String? = "",
    var reimbursementDetailsType: ReimbursementDetailsType
)

enum class ReimbursementDetailsType(val value:Int){
    TWO_LINE(1),
    MULTI_LINE(2),
    DIVIDER(3)
}
