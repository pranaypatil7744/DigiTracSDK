package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertEpfRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class POBEpfUseCase
constructor(private val repository: PobInsertEpfRepository):
    UseCase<POBInsertEpfResponseModel, Any?>()
{
    override suspend fun run(params: Any?): POBInsertEpfResponseModel {
        return repository.callPobInsertEpfApi(params as POBInsertEpfModel)
    }
}