package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class ReimbursementListResponseModel(
    var ReimbursementVoucherListDetails:ArrayList<ReimbursementVoucherListModel>? = null,
    var status:String? ="",
    var Message:String? ="",
)

@Keep
data class ReimbursementVoucherListModel(
    var AssociateReimbursementId:String? = "",
    var GNETAssociateID:String? = "",
    var VoucherNo:String? = "",
    var ApprovalRemark:String? = "",
    var CreatedDate:String? = "",
    var TotalAmount:String? = "",
    var ApprovalStatus:String? = "",
    var ApproverName:String? = "",
    var ApprovedDate:String? = "",
    var ApproverNameL2:String? = "",
    var ApprovedDateL2:String? = "",
    var ApprovalStatusL2:String? = "",
    var ApproverNameL3:String? = "",
    var ApprovedDateL3:String? = "",
    var ApprovalStatusL3:String? = "",
    var ApprovalRemarkL2:String? = "",
    var ApprovalRemarkL3:String? = "",
    var FilePath1:String? = "",
    var PaidStatus:String? = "",
    var PaidDate:String? = "",
)

@Keep
data class ReimbursementListRequestModel(
    var GNETAssociateId:String ="",
    var InnovId:String? =""
)
