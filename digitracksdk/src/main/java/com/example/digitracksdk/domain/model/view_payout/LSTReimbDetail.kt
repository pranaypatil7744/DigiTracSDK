package com.example.digitracksdk.domain.model.view_payout

import androidx.annotation.Keep

@Keep
data class LSTReimbDetail(
    val AssociateID: String? ="",
    val Month: String?="",
    val ReimbursementAmount: String?="",
    val ReimbursementID: String?="",
    val ReleaseDate: String?="",
    val Type: String?="",
    val Year: String?=""
)