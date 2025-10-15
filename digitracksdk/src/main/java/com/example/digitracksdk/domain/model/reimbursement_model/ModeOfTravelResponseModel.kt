package com.example.digitracksdk.domain.model.reimbursement_model

import androidx.annotation.Keep

@Keep
data class ModeOfTravelResponseModel(
    var ModeOfTravelDetails:ArrayList<ModeOfTravelDetailsModel>? = ArrayList(),
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class ModeOfTravelDetailsModel(
    var ModeOFTravelId:Int? = 0,
    var ModeOfTravle:String? =""
)

@Keep
data class ModeOfTravelRequestModel(
    var EmployeeID:String ="",
    var ReimbursementCategoryId:Int =0,
)
