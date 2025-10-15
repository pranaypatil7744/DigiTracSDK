package com.example.digitracksdk.presentation.my_profile.model

import androidx.annotation.Keep
import com.example.digitracksdk.presentation.my_profile.adapter.MyProfileAdapter

@Keep
data class ProfileModel(
    var title: String? = "",
    var value: String? = "",
    var summaryDetailsType: MyProfileAdapter.SummaryDetailsType
)
