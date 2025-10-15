package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class POBInsertEpfModel(
    var EmployeesPensionSchemeCheck: String ="",
    var EmployeesPensionSchemeCheck1952: String ="",
    var EmployeesPfCheck: String="",
    var ExistPFNo: String="",
    var ExistUANNo: String="",
    var ExitDate: String="",
    var InnovID: String="",
    var NomineeAddress: String="",
    var NomineeName: String="",
    var NomineePercentageAllocation: String=""
)


@Keep
data class POBInsertEpfResponseModel(
    val status: String?="",
    val Message: String?="",
    val OTP: String?="",
    val TokenID: String?="",
    val InnovID: String?=""
)