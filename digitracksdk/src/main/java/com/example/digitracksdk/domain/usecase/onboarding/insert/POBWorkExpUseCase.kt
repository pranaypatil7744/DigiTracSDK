package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertWorkExpRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class POBWorkExpUseCase
constructor(private val repository: PobInsertWorkExpRepository):
    UseCase<POBInsertWorkExpResponseModel, Any?>()
{
    override suspend fun run(params: Any?): POBInsertWorkExpResponseModel {
        return repository.callPobInsertWorkExpApi(params as POBInsertWorkExpModel)
    }
}