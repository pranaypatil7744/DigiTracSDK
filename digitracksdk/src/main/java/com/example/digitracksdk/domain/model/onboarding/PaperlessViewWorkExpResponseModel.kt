package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class PaperlessViewWorkExpResponseModel(
    val CompanyName: String?="",
    val CurrentlyEmployed: String? ="",
    val Designation: String?="",
    val EndDate: String?="",
    val InnovID: String?="",
    val IsFresher: String?="",
    val LstWorkExp: ArrayList<ListWorkExpModel>? = ArrayList(),
    val Message: String?="",
    val Payment: String?="",
    val Role: String?="",
    val StartDate: String?="",
    val TotalExpInYear: String?="",
    val TotalExpMonth: String?="",
    val status: String?="",
    val totalRelevantExpMonth: String?="",
    val totalRelevantExpYear: String?=""
)


@Keep
data class ListWorkExpModel(
    val CompanyName: String? ="",
    val CurrentlyEmployed: String? ="",
    val Designation: String? ="",
    val EndDate: String? ="",
    val InnovID: String? ="",
    val IsFresher: String?="",
    val LstWorkExp: String? ="",
    val Message: String?="",
    val Payment: String?="",
    val Role: String?="",
    val StartDate: String?="",
    val TotalExpInYear: String? ="",
    val TotalExpMonth: String?="",
    val status: String?="",
    val totalRelevantExpMonth: String?="",
    val totalRelevantExpYear: String?=""
):Serializable