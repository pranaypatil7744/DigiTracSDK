package com.example.digitracksdk.domain.model.view_payout


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ViewPayoutRequest(
    @SerializedName("FromDate") var fromDate: String ="",
    @SerializedName("GNETAssociateID") var gNETAssociateID: String ="",
    @SerializedName("ToDate") var toDate: String=""
)