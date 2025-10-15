package com.example.digitracksdk.domain.usecase.onboarding

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewCandidateReferenceDetailsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class PaperlessViewCandidateReferenceDetailsUseCase
constructor(private val repository: PaperlessViewCandidateReferenceDetailsRepository):
    UseCase<PaperlessViewGetCandidateReferenceDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): PaperlessViewGetCandidateReferenceDetailsResponseModel {
        return repository.callViewCandidateReferenceDetailsApi(params as InnovIDRequestModel)
    }
}