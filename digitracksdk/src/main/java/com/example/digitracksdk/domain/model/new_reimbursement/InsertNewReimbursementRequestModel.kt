package com.example.digitracksdk.domain.model.new_reimbursement

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class InsertNewReimbursementRequestModel(
    @SerializedName("AssociateReimbursementId") var AssociateReimbursementId: String? = null,
    @SerializedName("GNETAssociateId") var GNETAssociateId: String? = null,
    @SerializedName("Amount") var Amount: Double? = null,
    @SerializedName("BillDate") var BillDate: String? = null,
    @SerializedName("BillNo") var BillNo: String? = null,
    @SerializedName("ClaimDate") var ClaimDate: String? = null,
    @SerializedName("ClaimMonth") var ClaimMonth: String? = null,
    @SerializedName("ClaimYear") var ClaimYear: String? = null,
    @SerializedName("Extn") var Extn: String? = null,
    @SerializedName("FilePath") var FilePath: String? = null,
    @SerializedName("FromDate") var FromDate: String? = null,
    @SerializedName("FromLocation") var FromLocation: String? = null,
    @SerializedName("GrossAmount") var GrossAmount: Double? = null,
    @SerializedName("ModeOfTravelId") var ModeOfTravelId: Int? = null,
    @SerializedName("NameOfPlace") var NameOfPlace: String? = null,
    @SerializedName("ReimbursementCategoryId") var ReimbursementCategoryId: String? = null,
    @SerializedName("reimbursementCategoryName") var reimbursementCategoryName: String? = null,
    @SerializedName("Remark") var Remark: String? = null,
    @SerializedName("TaxAmount") var TaxAmount: Double? = null,
    @SerializedName("ToDate") var ToDate: String? = null,
    @SerializedName("ToLocation") var ToLocation: String? = null,
    @SerializedName("StartKM") var StartKM: String? = null,
    @SerializedName("EndKM") var EndKM: String? = null,
    @SerializedName("ReimbursementSubCategoryId") var ReimbursementSubCategoryId: String? = null
)

@Keep
data class InsertNewReimbursementResponseModel(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("Message" ) var Message : String? = null
)
