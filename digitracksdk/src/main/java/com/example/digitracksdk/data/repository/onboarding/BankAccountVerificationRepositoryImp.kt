package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank_account_verification.BankAccountVerificationRepository

class BankAccountVerificationRepositoryImp constructor(
    private val apiServiceAssociate: ApiService,
    private val apiServiceNormal: ApiService
) :
    BankAccountVerificationRepository {
    override suspend fun callBankAccountVerificationApi(request: BankAccountVerificationRequestModel): BankAccountVerificationResponseModel {
        return apiServiceAssociate.callBankAccountVerificationApi(request)
    }

    override suspend fun callBankAccountVerificationDetailsApi(request: BankAccountVerificationDetailsRequestModel): BankAccountVerificationDetailsResponseModel {
        return apiServiceNormal.callGetBankAccountVerificationDetailsApi(request)
    }

}