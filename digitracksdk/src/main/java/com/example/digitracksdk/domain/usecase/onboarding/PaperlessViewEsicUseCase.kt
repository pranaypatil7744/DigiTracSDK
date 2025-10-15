package com.example.digitracksdk.domain.usecase.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEsicResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewEsicRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class PaperlessViewEsicUseCase
constructor(private val repository: PaperlessViewEsicRepository):
    UseCase<PaperlessViewEsicResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewEsicResponseModel {
        return repository.callViewEsicApi(params as InnovIDRequestModel)
    }
}