package com.example.digitracksdk.domain.model.resignation

import androidx.annotation.Keep

@Keep
data class ResignationListResponseModel(
    var lstResignationDetails:ArrayList<ResignationsListModel>? = ArrayList(),
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class ResignationsListModel(
    var ResignationCategory:String? = "",
    var DateOfResignation:String? = "",
    var PreferredLWD:String? = "",
    var ExpectedLastWorkingDate:String? = "",
    var Remarks:String? = "",
    var RMApprovedLWD:String? = "",
    var ActionStatus:String? = "",
    var ActionDate:String? = "",
    var ActionBy:String? = "",
)

@Keep
data class ResignationListRequestModel(
    var GNETAssociateID:String = ""
)
