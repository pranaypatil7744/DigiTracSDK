package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class PaperlessFamilyDetailsModel(
    var CurrentAddress: String?="",
    var DateOfBirth: String?="",
    var ESIC: String?="",
    var FamilyMemberName: String?="",
    var Gratuity: String?="",
    var InnovID: String?="",
    var Insurance: String?="",
    var IsActive: String?="",
    var IsNominee: String?="",
    var IsResidingWithYou: String?="",
    var Occupation: String?="",
    var PF: String?="",
    var RelationshipId: String?=""
)

@Keep
data class PaperlessFamilyDetailsResponseModel(
    var status: String?="",
    var Message: String?="",
    var OTP: String?="",
    var TokenID: String?="",
    var InnovID: String?=""
)

