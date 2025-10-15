package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class InsertReimbursementRequestModel(
    var AssociateReimbursementDetailList:ArrayList<AssociateReimbursementDetailListModel>? = ArrayList(),
    var Extn1:String? = "",
    var FilePath1:String? = "",
    var GNETAssociateId:String? = "",
    var Remark:String? = ""
)
@Keep
data class AssociateReimbursementDetailListModel(
    var Amount: Double = 0.0,
    var BillDate:String? = "",
    var BillNo:String? = "",
    var ClaimDate:String? = "",
    var ClaimMonth:String? = "",
    var ClaimYear:String? = "",
    var dailyMileageLimit:String? = "",
    var Extn:String? = "",
    var FilePath:String? = "",
    var FromDate:String? = "",
    var FromLocation:String? = "",
    var GrossAmount:Double? = 0.0,
    var ModeOfTravelId:Int? = 0,
    var NameOfPlace:String? = "",
    var ReimbursementCategoryId:Int? = 0,
    var reimbursementCategoryName:String? = "",
    var ReimbursementSubCategoryId:Int? = 0,
    var reimbursementSubCategoryName:String? = "",
    var Remark:String? = "",
    var setModeOfTravelName:String? = "",
    var TaxAmount:Double? = 0.0,
    var ToDate:String? = "",
    var ToLocation:String? = "",
    var StartKm:String ="",
    var EndKm:String ="",
)

@Keep
data class InsertReimbursementResponseModel(
    var status:String? = "",
    var Message:String? = ""
)
