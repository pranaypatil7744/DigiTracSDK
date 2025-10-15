package com.example.digitracksdk.domain.model.onboarding

import androidx.annotation.Keep


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:49
 */
@Keep
data class PaperlessViewEpfResponseModel(

    val InnovID: String? = "",
    val ExistUANNo: String? = "",
    val ExistPFNo: String? = "",
    val ExitDate: String? = "",
    val EmployeesPfCheck:String? = "",
    val EmployeesPensionSchemeCheck:String? = "",
    val EmployeesPensionSchemeCheck1952:String? = "",
    val status: String? = "",
    val Message: String? = ""

)
