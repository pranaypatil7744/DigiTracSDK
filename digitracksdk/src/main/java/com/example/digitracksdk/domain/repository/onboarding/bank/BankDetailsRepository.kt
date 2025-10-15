package com.example.digitracksdk.domain.repository.onboarding.bank

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.ChequeValidationResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.GetBankListResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.PaperlessViewBankDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsResponseModel

interface BankDetailsRepository {

    suspend fun callViewBankDetailsApi(request: InnovIDRequestModel): PaperlessViewBankDetailsResponseModel

    suspend fun callBankListApi(): GetBankListResponseModel

    suspend fun callChequeValidationApi(request: CommonRequestModel): ChequeValidationResponseModel

    suspend fun callUpdateBankDetailsApi(request: UpdateBankDetailsRequestModel): UpdateBankDetailsResponseModel

}

