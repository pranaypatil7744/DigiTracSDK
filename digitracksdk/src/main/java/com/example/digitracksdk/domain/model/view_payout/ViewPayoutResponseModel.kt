package com.example.digitracksdk.domain.model.view_payout

import androidx.annotation.Keep

@Keep
data class ViewPayoutResponseModel(
    val LSTReimbDetails: ArrayList<LSTReimbDetail>?= ArrayList(),
    val Message: String ="",
    val Status: String =""
)