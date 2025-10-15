package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class UpdatePendingReimbursementResponseModel(
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class UpdatePendingReimbursementRequestModel(
    var AssociateID:String = "",
    var AssociateReimbursementID:String = "",
    var Remark:String = ""
)
