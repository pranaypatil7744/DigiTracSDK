package com.example.digitracksdk.domain.usecase.onboarding.education_details

import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamRequestModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamResponseModel
import com.example.digitracksdk.domain.repository.onboarding.education_details.EducationDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 20-10-2023.
 */
class EducationalStreamUseCase
constructor(val repository: EducationDetailsRepository)
    : UseCase<EducationalStreamResponseModel, Any?>()

{
    override suspend fun run(params: Any?): EducationalStreamResponseModel {
      return repository.callEducationalStreamApi(params as EducationalStreamRequestModel)
    }
}