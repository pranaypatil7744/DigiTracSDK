package com.example.digitracksdk.domain.model.onboarding.insert

import androidx.annotation.Keep

@Keep
data class POBInsertESICDetailsModel(
    var BranchOfficeName: String ="",
    var DispensaryName: String ="",
    var DoYouHaveAnOldESICNo: String ="",
    var ESICNo: String ="",
    var EmpCode: String ="",
    var InnovID: String ="",
    var InsuranceNo: String =""
)
@Keep
data class POBInsertEsicResponseModel(
    var status: String? ="",
    var Message: String? ="",
)

