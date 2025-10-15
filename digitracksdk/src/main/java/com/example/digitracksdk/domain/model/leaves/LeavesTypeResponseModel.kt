package com.example.digitracksdk.domain.model.leaves

import androidx.annotation.Keep

@Keep
data class LeavesTypeResponseModel(
    var LstAttnRegularizationType:ArrayList<ListAttnRegularizationType>? = ArrayList(),
    var Status:String? = "",
    var Message:String? = "",
)

@Keep
data class ListAttnRegularizationType(
    var RegularizationTypeID:Int? =0,
    var RegularizationType:String? =""
)
