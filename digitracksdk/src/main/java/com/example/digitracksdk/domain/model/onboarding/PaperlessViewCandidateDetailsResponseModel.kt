package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep

@Keep
data class PaperlessViewCandidateDetailsResponseModel(
    val AadharNo: String? = "",
    val Address1: String? = "",
    val Address2: String? = "",
    val Address3: String? = "",
    val BloodGroup: String? = "",
    val EmailId: String? = "",
    val EmergencyContactName: String? = "",
    val EmergencyContactNo: String? = "",
    val Gender: String? = "",
    val InnovId: String? = "",
    val Location: String? = "",
    val MaritalStatus: String? = "",
    val Message: String? = "",
    val NoOfChildren: String? = "",
    val Pincode: String? = "",
    val status: String? = "",
    val StateId: Int? = 0,
    val StateName: String? = "",
    val AlternateNoFlag: String? = "",
    val AlternateNo: String? = ""

)