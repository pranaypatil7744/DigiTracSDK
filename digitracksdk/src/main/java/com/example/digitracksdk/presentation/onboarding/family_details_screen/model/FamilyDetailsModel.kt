package com.example.digitracksdk.presentation.onboarding.family_details_screen.model

import androidx.annotation.Keep
import com.example.digitracksdk.Constant
import java.io.Serializable

@Keep
data class FamilyDetailsModel(
    var name: String? = "",
    var designation: String? = "",
    var dateOfBirth: String? = "",
    var gender: Constant.Gender,

    var isNominee: String? = "",
    var pf: String? = "",
    var esic: String? = "",
    var gratuity: String? = "",
    var insurance: String? = ""
):Serializable


@Keep
data class FamilyDetailsRecyclerModel(
    val title: String? = "",
    val hint: String? = "",
    var value: Any? = null,
    var icon: Int? = 0,
    var itemType: FamilyViewType
)

enum class FamilyViewType(val value: Int) {
    SPINNER(1),
    EDIT_TEXT(2),
    MULTI_EDITTEXT(3)
}

