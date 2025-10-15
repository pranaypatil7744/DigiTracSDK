package com.example.digitracksdk.domain.model.resignation


import androidx.annotation.Keep

@Keep
data class RevokeResignationResponseModel(
    var Message: String?="",
    var status: String?=""
)

@Keep
data class RevokeResignationRequestModel(
    var DateOfResignation: String?="",
    var GNETAssociateId: String?="",
    var Reason: String?=""

)