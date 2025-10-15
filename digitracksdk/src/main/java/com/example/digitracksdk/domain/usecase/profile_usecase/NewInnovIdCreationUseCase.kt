package com.example.digitracksdk.domain.usecase.profile_usecase

import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationRequestModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class NewInnovIdCreationUseCase constructor(private val profileRepository: ProfileRepository) :
    UseCase<NewInnovIdCreationResponseModel, Any?>() {
    override suspend fun run(params: Any?): NewInnovIdCreationResponseModel {
        return profileRepository.callInnovIDCreationNewApi(params as NewInnovIdCreationRequestModel)
    }
}