package com.example.digitracksdk.domain.model.onboarding.bank_model

import androidx.annotation.Keep

@Keep
data class UpdateBankDetailsResponseModel(
    var status:String? ="",
    var Message:String? ="",
    var OTP:String? ="",
    var TokenID:String? ="",
    var InnovID:String? ="",
)

@Keep
data class UpdateBankDetailsRequestModel(
    var AccountNumber:String? ="",
    var BankName:String? ="",
    var BranchDetails:String? ="",
    var ChequeDocImageArr:String? ="",
    var Extn:String? ="",
    var IFSCcode:String? ="",
    var InnovID:String? ="",
    var PANNumber:String? ="",
)
