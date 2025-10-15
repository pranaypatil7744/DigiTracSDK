package com.example.digitracksdk.domain.usecase.onboarding.bank_account_verification

import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank_account_verification.BankAccountVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class BankAccountVerificationUseCase constructor(private val bankAccountVerificationRepository: BankAccountVerificationRepository) :
    UseCase<BankAccountVerificationResponseModel, Any?>() {
    override suspend fun run(params: Any?): BankAccountVerificationResponseModel {
        return bankAccountVerificationRepository.callBankAccountVerificationApi(params as BankAccountVerificationRequestModel)
    }
}