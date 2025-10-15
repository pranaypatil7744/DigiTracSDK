package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.innov.digitrac.domain.model.home_model.HomeBannerRequestModel
import com.innov.digitrac.domain.model.home_model.HomeBannerResponseModel
import com.example.digitracksdk.domain.repository.home_repository.HomeBannerRepository

class HomeBannerRepositoryImp(var apiService: ApiService): HomeBannerRepository {
    override suspend fun callHomeBannerApi(request: HomeBannerRequestModel): HomeBannerResponseModel {
        return apiService.callHomeDashboardBannerApi(request)
    }
}