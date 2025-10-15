package com.example.digitracksdk.domain.repository.home_repository

import com.innov.digitrac.domain.model.home_model.HomeBannerRequestModel
import com.innov.digitrac.domain.model.home_model.HomeBannerResponseModel

interface HomeBannerRepository {

    suspend fun callHomeBannerApi(request:HomeBannerRequestModel):HomeBannerResponseModel
}