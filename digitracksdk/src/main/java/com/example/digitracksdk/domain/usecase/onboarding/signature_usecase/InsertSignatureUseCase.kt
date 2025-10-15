package com.example.digitracksdk.domain.usecase.onboarding.signature_usecase

import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureRequestModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureResponseModel
import com.example.digitracksdk.domain.repository.onboarding.signature.PaperlessViewGetSignatureRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertSignatureUseCase constructor(private val paperlessViewGetSignatureRepository: PaperlessViewGetSignatureRepository) :
    UseCase<InsertSignatureResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertSignatureResponseModel {
        return paperlessViewGetSignatureRepository.callInsertSignatureApi(params as InsertSignatureRequestModel)
    }
}