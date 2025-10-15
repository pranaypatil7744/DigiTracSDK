package com.example.digitracksdk.domain.usecase.onboarding.family_details

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewFamilyDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.family_details.FamilyDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewFamilyDetailsUseCase
constructor(private val repository: FamilyDetailsRepository):
    UseCase<PaperlessViewFamilyDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewFamilyDetailsResponseModel {
        return repository.callViewViewFamilyDetailsApi(params as InnovIDRequestModel)
    }
}