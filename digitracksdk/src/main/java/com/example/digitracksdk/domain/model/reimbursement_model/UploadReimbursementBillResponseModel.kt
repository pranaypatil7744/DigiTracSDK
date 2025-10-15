package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class UploadReimbursementBillResponseModel(
 var status:String? = "",
 var Message:String? = "",
)

@Keep
data class UploadReimbursementBillRequestModel(
 var AssociateId:String= "",
 var AssociateReimbursementId:String= "",
 var Extn:String= "",
 var FilePath:String= "",
)
