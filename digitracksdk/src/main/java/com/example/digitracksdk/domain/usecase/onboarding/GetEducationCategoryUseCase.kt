package com.example.digitracksdk.domain.usecase.onboarding

import com.example.digitracksdk.domain.model.onboarding.GetEducationCategoryResponseModel
import com.example.digitracksdk.domain.repository.onboarding.education_details.EducationDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetEducationCategoryUseCase
constructor(private val repository: EducationDetailsRepository):
    UseCase<GetEducationCategoryResponseModel, Any?>()
{
    override suspend fun run(params: Any?): GetEducationCategoryResponseModel {
        return repository.callGetEducationCategoryApi()
    }
}