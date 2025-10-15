package com.example.digitracksdk.domain.usecase.onboarding.bank_account_verification

import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank_account_verification.BankAccountVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 09-10-2023.
 */
class BankAccountVerificationDetailsUseCase
constructor(private val repository: BankAccountVerificationRepository) : UseCase<BankAccountVerificationDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): BankAccountVerificationDetailsResponseModel {
      return repository.callBankAccountVerificationDetailsApi(params as BankAccountVerificationDetailsRequestModel)
    }
}