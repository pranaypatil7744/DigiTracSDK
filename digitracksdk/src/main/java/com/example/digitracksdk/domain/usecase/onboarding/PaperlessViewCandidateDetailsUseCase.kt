package com.example.digitracksdk.domain.usecase.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewCandidateDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewCandidateDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class PaperlessViewCandidateDetailsUseCase
constructor(private val repository: PaperlessViewCandidateDetailsRepository):
    UseCase<PaperlessViewCandidateDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewCandidateDetailsResponseModel {
        return repository.callViewCandidateDetailsApi(params as InnovIDRequestModel)
    }
}