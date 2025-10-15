package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertESICDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEsicResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertEsicRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class POBEsicUseCase
constructor(private val repository: PobInsertEsicRepository):
    UseCase<POBInsertEsicResponseModel, Any?>()
{
    override suspend fun run(params: Any?): POBInsertEsicResponseModel {
        return repository.callPobInsertEsicApi(params as POBInsertESICDetailsModel)
    }
}