package com.example.digitracksdk.domain.usecase.onboarding.kyc

import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentListResponseModel
import com.example.digitracksdk.domain.repository.onboarding.kyc.KYCRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class KycDocumentsUseCase
constructor(private val repository: KYCRepository): UseCase<KycDocumentListResponseModel, Any?>()
{
    override suspend fun run(params: Any?): KycDocumentListResponseModel {
        return repository.callDTGetKYCDocsApi(params as EmpIDRequestModel)
    }
}