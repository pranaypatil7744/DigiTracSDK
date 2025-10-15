package com.example.digitracksdk.domain.usecase.profile_usecase

import com.example.digitracksdk.domain.model.profile_model.ValidCandidateRequestModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ValidCandidateUseCase constructor(private val profileRepository: ProfileRepository) :
    UseCase<ValidCandidateResponseModel, Any?>() {
    override suspend fun run(params: Any?): ValidCandidateResponseModel {
        return profileRepository.callValidCandidateApi(params as ValidCandidateRequestModel)
    }
}