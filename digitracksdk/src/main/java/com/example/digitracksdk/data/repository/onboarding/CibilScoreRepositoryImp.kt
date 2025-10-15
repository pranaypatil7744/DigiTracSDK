package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreRequestModel
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreResponseModel
import com.example.digitracksdk.domain.repository.onboarding.cibil_score.CibilScoreRepository

class CibilScoreRepositoryImp(var apiService: ApiService) : CibilScoreRepository
{

    override suspend fun callCibilScoreApi(request: GetCibilScoreRequestModel): GetCibilScoreResponseModel {
        return apiService.callGetCibilScoreApi(request)
    }
}