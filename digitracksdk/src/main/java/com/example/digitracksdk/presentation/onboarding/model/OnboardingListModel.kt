package com.example.digitracksdk.presentation.onboarding.model

import androidx.annotation.Keep

@Keep
data class OnboardingListModel(
    var itemName:String? = "",
    var itemIcon:Int? = null,
    var lastUpdateDate:String? = "",
    var status: OnboardingStatus
)
enum class OnboardingStatus(val value:Int){
    COMPLETED(1),
    IN_COMPLETED(2),
}
