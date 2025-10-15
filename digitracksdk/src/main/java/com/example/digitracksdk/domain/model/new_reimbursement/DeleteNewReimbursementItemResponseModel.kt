package com.example.digitracksdk.domain.model.new_reimbursement

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DeleteNewReimbursementItemResponseModel(
    var Status: String? = "",
    var Message: String? = ""
)

@Keep
data class DeleteNewReimbursementItemRequestModel(
    @SerializedName("AssociateId") var AssociateId: String = "",
    @SerializedName("AssociateReimbursementDetailId") var AssociateReimbursementDetailId: String = "",
    @SerializedName("User_ID") var UserID: String = ""

)
