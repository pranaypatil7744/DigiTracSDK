package com.example.digitracksdk.domain.usecase.profile_usecase

import com.example.digitracksdk.domain.model.profile_model.CityListRequestModel
import com.example.digitracksdk.domain.model.profile_model.CityListResponseModel
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CityListUseCase constructor(private val profileRepository: ProfileRepository):
    UseCase<CityListResponseModel, Any?>() {
    override suspend fun run(params: Any?): CityListResponseModel {
        return profileRepository.callCityListApi(params as CityListRequestModel)
    }
}