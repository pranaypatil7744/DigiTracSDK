package com.example.digitracksdk.domain.usecase.onboarding.bank_usecase

import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank.BankDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UpdateBankDetailsUseCase constructor(private val bankDetailsRepository: BankDetailsRepository):
    UseCase<UpdateBankDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): UpdateBankDetailsResponseModel {
        return bankDetailsRepository.callUpdateBankDetailsApi(params as UpdateBankDetailsRequestModel)
    }
}