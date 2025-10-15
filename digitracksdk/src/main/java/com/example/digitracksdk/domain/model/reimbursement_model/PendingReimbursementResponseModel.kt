package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class PendingReimbursementResponseModel(
    var LSTPendingFinalVoucher:ArrayList<PendingVoucherModel> = ArrayList(),
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class PendingVoucherModel(
    var AssociateId:String? = "",
    var GNETAssociateID:String? = "",
    var VoucherNo:String? = "",
    var AR_CreatedON:String? = "",
    var TotalAmount:String? = "",
    var AR_AssociateReimbursementId:String? = "",
    var VoucherMonth:String? = "",
    var VoucherYear:String? = "",
)

@Keep
data class PendingReimbursementRequestModel(
    var AssociateID:String="",
    var MappingID:String=""
)
