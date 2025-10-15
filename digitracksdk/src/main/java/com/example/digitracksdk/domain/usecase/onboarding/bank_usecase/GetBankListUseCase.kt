package com.example.digitracksdk.domain.usecase.onboarding.bank_usecase

import com.example.digitracksdk.domain.model.onboarding.bank_model.GetBankListResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank.BankDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetBankListUseCase constructor(private val bankDetailsRepository: BankDetailsRepository) :
    UseCase<GetBankListResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetBankListResponseModel {
        return bankDetailsRepository.callBankListApi()
    }
}