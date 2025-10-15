package com.example.digitracksdk.domain.model.onboarding.bank_model

import androidx.annotation.Keep

@Keep
data class GetBankListResponseModel(
    var BankList:ArrayList<BankListModel>?= ArrayList(),
    var Status:String? ="",
    var Message:String? ="",
)
@Keep
data class BankListModel(
    var BankId:String? ="",
    var BanKName:String? ="",
)

