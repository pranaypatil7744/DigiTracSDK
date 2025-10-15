package com.example.digitracksdk.domain.usecase.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewWorkExpResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewWorkExpDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class PaperlessViewWorkExpDetailsUseCase
constructor(private val repository: PaperlessViewWorkExpDetailsRepository):
    UseCase<PaperlessViewWorkExpResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewWorkExpResponseModel {
        return repository.callViewWorkExpDetailsApi(params as InnovIDRequestModel)
    }
}