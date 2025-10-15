package com.example.digitracksdk.domain.model.refer_a_friend

import androidx.annotation.Keep

@Keep
data class BranchDetailsResponseModel(
    var LstBranchDetails:ArrayList<BranchListModel>? = ArrayList(),
    var Status:String? ="",
    var Message:String? =""
)

@Keep
data class BranchListModel(
    var FacilityID:Int? =0,
    var FacilityName:String? =""
)

@Keep
data class BranchDetailsRequestModel(
    var MappingID:String = ""
)
