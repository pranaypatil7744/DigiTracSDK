package com.example.digitracksdk.domain.model.help_and_support

import androidx.annotation.Keep

@Keep
data class HelpAndSupportListRequestModel(
    var InnovID:String = ""
)

@Keep
data class HelpAndSupportListResponseModel(
    var lstAssociateIssueDetails:ArrayList<ListAssociateIssueDetails>? = ArrayList(),
    var status:String? = "",
    var Message:String? = "",
)

@Keep
data class ListAssociateIssueDetails(
    var InnovID:String? = "",
    var IssueDetails:String? = "",
    var IssueStatus:String? = "",
    var GNETAssociateID:String? = "",
    var ClientName:String? = "",
    var FacilityName:String? = "",
    var AssociateName:String? = "",
    var IssueCategoryName:String? = "",
    var IssueSubCategoryName:String? = "",
    var AssociateQueryId:String? = "",
    var CurrentIssueStatus:String? = "",
    var CreatedOn:String? = "",
    var LastUpdatedOn:String? =""
)