package com.example.digitracksdk.domain.model.profile_model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class InsertBasicDetailsResponseModel(
    var status: String? = "",
    var Message: String? = "",
    var OTP: String? = "",
    var TokenID: String? = "",
    var InnovID: String? = ""
)

@Keep
data class InsertBasicDetailsRequestModel(
    var NameAsPerAadhar: String = "",
    var AadharNo: String = "",
    var Gender: String = "",
    var DOB: String = "",
    var Mobile: String = "",
    var FatherName: String = "",
    var DocType: String = "",
    var AadharDocImageArr: String = "",
    var Extn: String = "",
):Serializable
