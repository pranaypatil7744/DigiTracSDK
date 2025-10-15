package com.example.digitracksdk.domain.usecase.onboarding.family_details

import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.family_details.FamilyDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertFamilyDetailsUseCase
constructor(private val repository: FamilyDetailsRepository):
    UseCase<PaperlessFamilyDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessFamilyDetailsResponseModel {
        return repository.callFamilyDetailsApi(params as PaperlessFamilyDetailsModel)
    }
}