package com.example.digitracksdk.domain.usecase.onboarding.kyc

import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListResponseModel
import com.example.digitracksdk.domain.repository.onboarding.kyc.KYCRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


class KycDocumentPendingListUseCase
constructor(private val repository: KYCRepository):
    UseCase<KycDocumentPendingListResponseModel, Any?>()
{
    override suspend fun run(params: Any?): KycDocumentPendingListResponseModel {
        return repository.callPobDocumentTypeKycApi(params as KycDocumentPendingListRequestModel)
    }
}