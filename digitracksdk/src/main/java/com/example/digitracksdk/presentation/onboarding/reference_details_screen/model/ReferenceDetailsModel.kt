package com.example.digitracksdk.presentation.onboarding.reference_details_screen.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class ReferenceDetailsModel(
    var name:String? = "",
    var email:String? = "",
    var mobile:String? = "",
    var isFriend:Boolean? = false,
):Serializable
