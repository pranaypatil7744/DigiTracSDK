package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class PaperlessViewFamilyDetailsResponseModel(
    val status: String? = "",
    val Message: String? = "",
    var lstFamily: ArrayList<ListFamily>? = ArrayList(),
)

@Keep
data class ListFamily(
    val RelationshipId: String? = "",
    val FamilyMemberName: String? = "",
    val DateOfBirth: String? = "",
    val Occupation: String? = "",
    val IsResidingWithYou: String? = "",
    val CurrentAddress: String? = "",
    val IsNominee:String? ="",
    val PF: String? = "",
    val ESIC: String? = "",
    val Insurance: String? = "",
    val Gratuity: String? = "",
    val IsActive: String? = "",
    val InnovID: String? = "",
    val Relation: String? = "",
    val SrNo: String? = ""
) : Serializable




