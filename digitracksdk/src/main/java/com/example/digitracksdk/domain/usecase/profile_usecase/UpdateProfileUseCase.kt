package com.example.digitracksdk.domain.usecase.profile_usecase

import com.example.digitracksdk.domain.model.profile_model.UpdateProfileRequestModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UpdateProfileUseCase constructor(private val profileRepository: ProfileRepository) :
    UseCase<UpdateProfileResponseModel, Any?>() {
    override suspend fun run(params: Any?): UpdateProfileResponseModel {
        return profileRepository.callUpdateProfileApi(params as UpdateProfileRequestModel)
    }
}