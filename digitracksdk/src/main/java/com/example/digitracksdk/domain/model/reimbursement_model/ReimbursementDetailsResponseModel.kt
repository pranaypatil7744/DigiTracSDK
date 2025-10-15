package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class ReimbursementDetailsResponseModel(
    var ListOfAssociateReimbursement:ArrayList<ReimbursementDetailsModel>? = ArrayList(),
    var status:String? ="",
    var Message:String? ="",
)

@Keep
data class ReimbursementDetailsModel(
    var AssociateReimbursementDetailId:String? = "",
    var GNETAssociateId:String? = "",
    var ReimbursementCategory:String? = "",
    var ReimbursementSubCategory:String? = "",
    var ReimbursementCategoryCode:String? = "",
    var BillNo:String? = "",
    var BillDate:String? = "",
    var ModeOfTravel:String? = "",
    var FromLocation:String? = "",
    var ToLocation:String? = "",
    var FromDate:String? = "",
    var ToDate:String? = "",
    var Remark:String? = "",
    var Amount:String? = "",
    var CreatedDate:String? = "",
    var ClaimDate:String? = "",
    var GrossAmount:String? = "",
    var TaxAmount:String? = "",
    var StartKM:String? = "",
    var EndKM:String? = "",
    var FilePath1:String? = "",
    var ClaimMonth:String? = "",
    var ClaimYear:String? = "",
):Serializable

@Keep
data class ReimbursementDetailsRequestModel(
    var AssociateReimbursementId:String = ""
)

@Keep
data class ReimbursementBillResponseModel(
    var AssociateReimbursementFileList:ArrayList<ReimbursementBillDetailsModel>? = ArrayList(),
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class ReimbursementBillDetailsModel(
    var AssociateReimbursementDocumentsId:String? = "",
    var FilePath:String? = "",
    var Extn:String? ="",
    var CreatedDate:String? = ""

)