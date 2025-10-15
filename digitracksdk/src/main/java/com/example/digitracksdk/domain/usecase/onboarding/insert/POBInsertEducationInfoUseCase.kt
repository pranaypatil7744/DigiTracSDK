package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoResponseModel
import com.example.digitracksdk.domain.repository.onboarding.education_details.EducationDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class POBInsertEducationInfoUseCase
constructor(private val repository: EducationDetailsRepository):
    UseCase<InsertEducationInfoResponseModel, Any?>()
{
    override suspend fun run(params: Any?): InsertEducationInfoResponseModel {
        return repository.callPobInsertEducationInfoApi(params as InsertEducationInfoRequestModel)
    }
}