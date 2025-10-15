package com.example.digitracksdk.domain.model.resignation


import androidx.annotation.Keep

@Keep
data class ResignationNoticePeriodResponseModel(
    var Message: String?="",
    var NoticePeriod: String?="",
    var status: String?=""
)

@Keep
data class ResignationNoticePeriodRequestModel(
    var  GNETAssociateId : String?=""
  )