package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class OfferLetterAcceptRejectResponseModel(
    var status:String? ="",
    var Message:String? =""
)

@Keep
data class OfferLetterAcceptRejectRequestModel(
    var AcceptStatus:String="",
    var GNETAssociateID:String="",
    var ImageDocArray:String?="",
    var OfferID:Int=0,
    var OfferPatternID:Int=0,
    var OfferType:String=""
)
