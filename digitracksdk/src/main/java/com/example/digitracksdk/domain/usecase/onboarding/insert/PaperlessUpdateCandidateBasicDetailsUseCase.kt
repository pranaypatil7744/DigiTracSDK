package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewCandidateDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PaperlessUpdateCandidateBasicDetailsUseCase
constructor(private val repository: PaperlessViewCandidateDetailsRepository):
    UseCase<PaperlessUpdateCandidateBasicDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessUpdateCandidateBasicDetailsResponseModel {
        return repository.callUpdateCandidateBasicDetailsApi(params as PaperlessUpdateCandidateBasicDetailsRequestModel)
    }
}