package com.example.digitracksdk.domain.usecase.profile_usecase

import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertBasicDetailsUseCase constructor(private val profileRepository: ProfileRepository) :
    UseCase<InsertBasicDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertBasicDetailsResponseModel {
        return profileRepository.callInsertBasicDetailsApi(params as InsertBasicDetailsRequestModel)
    }
}