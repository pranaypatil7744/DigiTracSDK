package com.example.digitracksdk.domain.usecase.onboarding.education_details

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEducationDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.education_details.EducationDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PaperlessViewEducationDetailsUseCase
constructor(private val repository: EducationDetailsRepository) :
    UseCase<PaperlessViewEducationDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): PaperlessViewEducationDetailsResponseModel {
        return repository.callViewViewEducationDetailsApi(params as InnovIDRequestModel)
    }
}