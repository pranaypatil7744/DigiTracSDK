package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:41
 */
@Keep
data class PaperlessViewEsicResponseModel (

    val InnovID: String? = "",
    val InsuranceNumber: String? = "",
    val PreviousEmployeeCodeNumber: String? = "",
    val DispensaryName: String? = "",
    val DoYouHaveAnOldESICNo: String? = "",
    val ExistESICNo: String? = "",
    val BranchOfficeName: String? = "",
    val status: String? = "",
    val Message: String? = ""

)

