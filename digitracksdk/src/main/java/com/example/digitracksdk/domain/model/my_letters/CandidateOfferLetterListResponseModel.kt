package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class CandidateOfferLetterListResponseModel(
    var InnovID:String? = "",
    var Offers:ArrayList<OffersModel>? = ArrayList(),
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class OffersModel(
    var CandidateType:String? ="",
    var ClientName:String? ="",
    var Designation:String? ="",
    var DriveID:String? ="",
    var EmployeeCode:String? ="",
    var InnovID:String? ="",
    var JoiningDate:String? ="",
    var Logo:String? ="",
    var OfferGenerated:String? ="",
    var OfferID:String? ="",
    var OfferName:String? ="",
    var OfferPatternId:String? ="",
    var OfferStatus:String? ="",
    var Status:String? ="",
    var WorkLocation:String? =""
)
