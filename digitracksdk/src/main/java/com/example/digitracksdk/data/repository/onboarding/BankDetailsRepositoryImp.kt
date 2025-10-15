package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.ChequeValidationResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.GetBankListResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.PaperlessViewBankDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.bank.BankDetailsRepository

class BankDetailsRepositoryImp
    (private val apiService: ApiService) : BankDetailsRepository {
    override suspend fun callViewBankDetailsApi(request: InnovIDRequestModel): PaperlessViewBankDetailsResponseModel {
        return apiService.callGetBankDetailsApi(request)
    }

    override suspend fun callBankListApi(): GetBankListResponseModel {
        return apiService.callBankListApi()
    }

    override suspend fun callChequeValidationApi(request: CommonRequestModel): ChequeValidationResponseModel {
        return apiService.callChequeValidationApi(request)
    }

    override suspend fun callUpdateBankDetailsApi(request: UpdateBankDetailsRequestModel): UpdateBankDetailsResponseModel {
        return apiService.callUpdateBankDetailsApi(request)
    }
}