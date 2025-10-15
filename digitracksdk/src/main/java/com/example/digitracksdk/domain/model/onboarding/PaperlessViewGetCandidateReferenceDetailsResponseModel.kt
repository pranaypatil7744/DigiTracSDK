package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class PaperlessViewGetCandidateReferenceDetailsResponseModel(
    val Message: String?= "",
    val lstReferenceDetails: ArrayList<LstReferenceDetail>?= ArrayList(),
    val status: String? =""
)

@Keep
data class LstReferenceDetail(
    val Address: String? = "",
    val CandidateID: String?="",
    val CandidateReferenceDetailID: String?="",
    val ContactNo: String?="",
    val EmailId: String?="",
    val InnovId: String?="",
    val Name: String?="",
    val ReferenceCategoryID: String?="",
    val ReferenceCategoryName: String?="",
    val Source: String? = ""
): Serializable