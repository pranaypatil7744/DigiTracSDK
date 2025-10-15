package com.example.digitracksdk.domain.model.profile_model

import androidx.annotation.Keep

@Keep
data class CityListResponseModel(
    val Citylist:ArrayList<CityListModel>? = ArrayList(),
    val Status:String? = "",
    val Message:String? = "",
)

@Keep
data class CityListModel(
    val CityID:Int? = 0,
    val CityName:String? = "",
)

@Keep
data class CityListRequestModel(
    val StateID:Int = 0
)
