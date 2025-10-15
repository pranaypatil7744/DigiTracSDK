package com.example.digitracksdk.domain.usecase.home_usecase

import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardResponseModel
import com.example.digitracksdk.domain.repository.home_repository.HomeRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class HomeDashboardUseCase constructor(private val homeRepository: HomeRepository) :
    UseCase<HomeDashboardResponseModel, Any?>() {
    override suspend fun run(params: Any?): HomeDashboardResponseModel {
        return homeRepository.callHomeDashboardApi(params as HomeDashboardRequestModel)
    }
}