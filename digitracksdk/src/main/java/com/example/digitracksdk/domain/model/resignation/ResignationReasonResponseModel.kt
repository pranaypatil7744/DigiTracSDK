package com.example.digitracksdk.domain.model.resignation


import androidx.annotation.Keep

@Keep
data class ResignationReasonResponseModel(
    var Message: String?="",
    var lstResignationReason: List<LstResignationReason>?=null,
    var status: String?=""
)

@Keep
data class LstResignationReason(
    var ResignationCategory: String?="",
    var ResignationCategoryId: Int?=0
)

@Keep
data class  ResignationReasonRequestModel(
    var GNETAssociateId : String?=""
   )