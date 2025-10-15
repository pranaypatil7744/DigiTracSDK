package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckModel
import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.CandidateProfileAckRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class CandidateProfileAckUseCase
constructor(private val repository: CandidateProfileAckRepository):
    UseCase<CandidateProfileAckResponseModel, Any?>()
{
    override suspend fun run(params: Any?): CandidateProfileAckResponseModel {
        return repository.callCandidateProfileAckApi(params as CandidateProfileAckModel)
    }
}