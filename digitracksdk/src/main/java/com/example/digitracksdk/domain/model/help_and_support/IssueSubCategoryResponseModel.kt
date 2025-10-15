package com.example.digitracksdk.domain.model.help_and_support

import androidx.annotation.Keep

@Keep
data class IssueSubCategoryResponseModel(
    var IssueCategoryID:Int? = 0,
    var IssueSubCategoryName:String? = "",
    var IssueSubCategoryId:Int? = 0,
    var Message:String? = ""
)

@Keep
data class IssueSubCategoryRequestModel(
    var IssueCategoryID:Int = 0
)
