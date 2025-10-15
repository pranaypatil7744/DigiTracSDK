package com.example.digitracksdk.domain.model.new_reimbursement

import androidx.annotation.Keep

@Keep
data class GetNewReimbursementListResponseModel(
    var LstReimbdetailsforVoucherGen: ArrayList<LstReimbdetailsforVoucherGenModel>? = null,
    var Message: String? = "",
    var Status: String? = ""
)

@Keep
data class LstReimbdetailsforVoucherGenModel(
    var AMOUNT: String? = "",
    var AssociateReimbursementDetailId: String? = "",
    var BillDate: String? = "",
    var BillNo: String? = "",
    var CreatedDate: String? = "",
    var FromLocation: String? = "",
    var GrossAmount: String? = "",
    var ReimbursementCategory: String? = "",
    var ReimbursementSubCategory: String? = "",
    var TaxAmount: String? = "",
    var ToLocation: String? = "",
    var isSelected: Boolean = false
)
