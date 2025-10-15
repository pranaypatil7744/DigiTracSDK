package com.example.digitracksdk.domain.model.resignation

import androidx.annotation.Keep

@Keep
data class ResignationCategoryResponseModel(
    var lstResignationCategory:ArrayList<ListResignationCategory>? = ArrayList(),
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class ListResignationCategory(
    var ResignationCategoryId:Int? = 0,
    var ResignationCategory:String? =""
)