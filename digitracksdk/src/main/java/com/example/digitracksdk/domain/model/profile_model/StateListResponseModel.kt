package com.example.digitracksdk.domain.model.profile_model

import androidx.annotation.Keep

@Keep
data class StateListResponseModel(
    val StateList:ArrayList<StateListModel>? = ArrayList(),
    val Status:String? = "",
    val Message:String? = "",
)
@Keep
data class StateListModel(
    val StateID:Int? =0,
    val StateName:String? ="",
)
