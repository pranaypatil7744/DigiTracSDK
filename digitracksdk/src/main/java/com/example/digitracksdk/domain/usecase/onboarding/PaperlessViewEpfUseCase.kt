package com.example.digitracksdk.domain.usecase.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEpfResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewEpfRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class PaperlessViewEpfUseCase
constructor(private val repository: PaperlessViewEpfRepository):
    UseCase<PaperlessViewEpfResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewEpfResponseModel {
        return repository.callViewEpfApi(params as InnovIDRequestModel)
    }
}