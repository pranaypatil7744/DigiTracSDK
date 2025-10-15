package com.example.digitracksdk.domain.usecase.onboarding.bank_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.ChequeValidationResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank.BankDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ChequeValidationUseCase constructor(private val bankDetailsRepository: BankDetailsRepository) :
    UseCase<ChequeValidationResponseModel, Any?>() {
    override suspend fun run(params: Any?): ChequeValidationResponseModel {
        return bankDetailsRepository.callChequeValidationApi(params as CommonRequestModel)
    }
}