package com.example.digitracksdk.domain.model.income_tax

import androidx.annotation.Keep

@Keep
data class IncomeTaxDeclarationResponseModel(
    var URL: String? = "",
    var Status: String? = "",
    var Message: String? = ""
)

@Keep
data class IncomeTaxDeclarationRequestModel(
    var InnovID: String = ""
)
