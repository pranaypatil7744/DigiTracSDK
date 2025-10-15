package com.example.digitracksdk.presentation.onboarding.candidate_details.model

import androidx.annotation.Keep

@Keep
data class CandidateDetails(
    var title: String? = "",
    var value: Any? = null,
    var itemType: CandidateItemType,
    var isEnable:Boolean = false
)


enum class CandidateItemType(val value: Int) {
    LABEL(1),
    TEXT(2),
    TEXT_EMAIL(3),
    SPINNER(4),
    NUMBER(5)
}