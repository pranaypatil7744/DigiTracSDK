package com.example.digitracksdk.domain.model.new_reimbursement
import androidx.annotation.Keep

@Keep
data class GenerateVoucherFromNewReimbursementRequestModel(
    var AssociateID: String,
    var AssociateReimbursementDetailID: String,
    var Remark: String? = ""
)


@Keep
data class GenerateVoucherFromNewReimbursementResponseModel(
    var Status: String? = "",
    var Message: String? = ""
)