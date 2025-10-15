package com.example.digitracksdk.presentation.my_letters.offer_letter.model

import androidx.annotation.Keep
import com.example.digitracksdk.presentation.my_letters.candidate_loi.model.CandidateLoiStatus
import java.io.Serializable

@Keep
data class OfferLetterModel(
    var title:String? = "",
    var designation:String? = "",
    var location:String? = "",
    var joiningDate:String? = "",
    var fileUrl:String? = "",
    var offerLetterStatus: CandidateLoiStatus,
    val CandidateType:String? = "",
    val OfferId:String? = "",
    val OfferPatternId:String? = "",
):Serializable

