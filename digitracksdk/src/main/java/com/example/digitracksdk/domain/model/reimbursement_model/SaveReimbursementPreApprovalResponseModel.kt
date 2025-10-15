package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class SaveReimbursementPreApprovalResponseModel(
    var status:String? ="",
    var Message:String? ="",
)

@Keep
data class SaveReimbursementPreApprovalRequestModel(
    var ApplicableForPeriod:String ="",
    var ApprovedAmount:Double =0.0,
    var ApprovedBy:String ="",
    var ApprovedDate:String ="",
    var Extn:String ="",
    var FilePath:String ="",
    var GnetAssociateId:String ="",
    var ReimbursementCategoryId:String ="",
    var ReimbursementDate:String ="",
)
