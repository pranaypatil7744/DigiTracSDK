package com.example.digitracksdk.domain.model.help_and_support

import androidx.annotation.Keep

@Keep
data class AssociateIssueResponseModel(
    var status:String? = "",
    var Message:String? = "",
    var OTP:String? = "",
    var TokenID:String? = "",
    var InnovID:String? = "",
)

@Keep
data class AssociateIssueRequestModel(
    var InnovID:String = "",
    var IssueCategoryId:Int= 0,
    var IssueSubCategoryId:Int = 0,
    var IssueHeader:String? = "",
    var IssueDetails:String? = "",
    var MappingID:String? = "",
)
