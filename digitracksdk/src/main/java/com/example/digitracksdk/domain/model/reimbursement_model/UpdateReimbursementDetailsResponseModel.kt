package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class UpdateReimbursementDetailsResponseModel(
    var status:String?= "",
    var Message:String?= ""
)

@Keep
data class UpdateReimbursementDetailsRequestModel(
    var AssociateId:Int = 0,
    var AssociateReimbursementDetailId:Int = 0,
    var AssociateReimbursementId:Int = 0,
    var BillDate:String = "",
    var BaseAmount:Double = 0.0,
    var TaxAmount:Double = 0.0,
    var GrossAmount:Double = 0.0,
    var BillNo:String = "",
    var ClaimDate:String = "",
    var EndKM:Int = 0,
    var FromDate:String = "",
    var FromLocation:String = "",
    var GNETAssociateId:String = "",
    var ModeOfTravelId:Int = 0,
    var NameOfPlace:String = "",
    var ReimbursementCategoryId:Int = 0,
    var ReimbursementSubCategoryId:Int = 0,
    var Remark:String? = "",
    var StartKM:Int = 0,
    var ToDate:String = "",
    var ToLocation:String = "",
    var UpdatedBy:String = "",
    var Amount:Double = 0.0,
    var categoryName:String = "",
)