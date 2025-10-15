package com.example.digitracksdk.presentation.home.reimbursements.model

import androidx.annotation.Keep

@Keep
data class ReimbursementModel(
    var title: String? = "",
    var description: String? = "",
    var approverName1: String? = "",
    var approvedDate1: String? = "",
    var approvalStatus1: String? = "",
    var approvalRemark1: String? = "",
    var approverName2: String? = "",
    var approvedDate2: String? = "",
    var approvalStatus2: String? = "",
    var approvalRemark2: String? = "",
    var approverName3: String? = "",
    var approvedDate3: String? = "",
    var approvalStatus3: String? = "",
    var approvalRemark3: String? = "",
    var type: ReimbursementType,
    var category: ReimbursementCategoryType,
    var amount: String? = "",
    var AssociateReimbursementId: String? = "",
    var createdDate: String? = "",
    var voucherNo:String? ="",
    var paidStatus:String? = "",
    var paidDate:String? = ""
)


enum class ReimbursementType(val value: Int) {
    AWAITING(1),
    APPROVED(2),
    REJECTED(3)

}

enum class ReimbursementCategoryType(val value: Int) {
    PUBLIC_TRANSPORT(1),
    SELF_VEHICLE(2),
    FOOD(3),
    LODGING(4)
}