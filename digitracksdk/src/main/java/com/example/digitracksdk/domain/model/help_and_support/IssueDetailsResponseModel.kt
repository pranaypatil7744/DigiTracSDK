package com.example.digitracksdk.domain.model.help_and_support

import androidx.annotation.Keep

@Keep
data class IssueDetailsResponseModel(
    var lstAssociateIssueUpdates:ArrayList<ListAssociateIssueUpdatesModel> = ArrayList(),
    var status:String? ="",
    var Message:String? =""
)

@Keep
data class ListAssociateIssueUpdatesModel(
    var AssociateQueryId:String? = "",
    var AssociateAqeryDetailId:String? = "",
    var Remark:String? = "",
    var LastUpdatedOn:String? = "",
    var LastUpdatedBy:String? = "",
    var QueryStatus:String? = "",
)

@Keep
data class IssueDetailsRequestModel(
    var AssociateQueryId:String = ""
)
