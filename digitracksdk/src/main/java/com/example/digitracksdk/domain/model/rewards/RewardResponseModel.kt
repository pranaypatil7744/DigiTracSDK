package com.example.digitracksdk.domain.model.rewards

import androidx.annotation.Keep

@Keep
data class RewardResponseModel(
    val Message: String?="",
    val Status : String?="",
    val associateRewardDetail: ArrayList<AssociateRewardDetail>? = null
)


@Keep
data class AssociateRewardDetail(
    var AssociateRewardId: Int?=0,
    var Month: String?="",
    var Purpose: String?="",
    var RewardCategory: String?="",
    var Url: String?="",
    var Year: String?=""
)

@Keep
data class RewardRequestModel(
    var GNETAssociateID: String?="",
)