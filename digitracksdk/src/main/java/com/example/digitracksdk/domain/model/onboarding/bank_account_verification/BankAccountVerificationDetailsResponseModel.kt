package com.example.digitracksdk.domain.model.onboarding.bank_account_verification

import androidx.annotation.Keep

/**
 * Created by Mo Khurseed Ansari on 03-05-2023.
 */


@Keep
data class BankAccountVerificationDetailsResponseModel(
    var InnovId: String? = "",
    var beneficiaryname: String? = "",
    var status: String? = "",
    var Message: String? = ""
)

@Keep
data class BankAccountVerificationDetailsRequestModel(
    var AccountNumber: String? = "",
    var IFSC: String? = "",
    var InnovId: String? = ""
)