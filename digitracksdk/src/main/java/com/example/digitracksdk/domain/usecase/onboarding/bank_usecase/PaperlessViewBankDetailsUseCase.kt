package com.example.digitracksdk.domain.usecase.onboarding.bank_usecase

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.PaperlessViewBankDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank.BankDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class PaperlessViewBankDetailsUseCase
constructor(private val repository: BankDetailsRepository):
    UseCase<PaperlessViewBankDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewBankDetailsResponseModel {
        return repository.callViewBankDetailsApi(params as InnovIDRequestModel)
    }
}