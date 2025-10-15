package com.example.digitracksdk.domain.usecase.onboarding.signature_usecase

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetSignatureResponseModel
import com.example.digitracksdk.domain.repository.onboarding.signature.PaperlessViewGetSignatureRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class PaperlessViewGetSignatureUseCase
constructor(private val repository: PaperlessViewGetSignatureRepository):
    UseCase<PaperlessViewGetSignatureResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewGetSignatureResponseModel {
        return repository.callViewGetSignatureApi(params as InnovIDRequestModel)
    }
}