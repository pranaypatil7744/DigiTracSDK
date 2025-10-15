package com.example.digitracksdk.domain.usecase.home_usecase

import com.innov.digitrac.domain.model.home_model.HomeBannerRequestModel
import com.innov.digitrac.domain.model.home_model.HomeBannerResponseModel
import com.example.digitracksdk.domain.repository.home_repository.HomeBannerRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class HomeBannerUseCase constructor(private val homeBannerRepository: HomeBannerRepository) :
    UseCase<HomeBannerResponseModel, Any?>() {
    override suspend fun run(params: Any?): HomeBannerResponseModel {
        return homeBannerRepository.callHomeBannerApi(params as HomeBannerRequestModel)
    }
}