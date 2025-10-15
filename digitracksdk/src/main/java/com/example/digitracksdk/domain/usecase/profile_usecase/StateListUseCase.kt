package com.example.digitracksdk.domain.usecase.profile_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.profile_model.StateListResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class StateListUseCase constructor(private val profileRepository: ProfileRepository) :
    UseCase<StateListResponseModel, Any?>() {
    override suspend fun run(params: Any?): StateListResponseModel {
        return profileRepository.callStateListApi(params as CommonRequestModel)
    }
}