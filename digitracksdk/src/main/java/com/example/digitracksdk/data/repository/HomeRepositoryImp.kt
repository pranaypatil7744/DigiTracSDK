package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.home_model.InductionTrainingResponseModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkRequestModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkResponseModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardResponseModel
import com.example.digitracksdk.domain.model.home_model.response.CheckBirthdayResponseModel
import com.example.digitracksdk.domain.repository.home_repository.HomeRepository

class HomeRepositoryImp(var apiService: ApiService): HomeRepository {
    override suspend fun callHomeDashboardApi(request: HomeDashboardRequestModel): HomeDashboardResponseModel {
        return apiService.callHomeDashboardApi(request)
    }

    override suspend fun callCheckBirthdayApi(request: CommonRequestModel): CheckBirthdayResponseModel {
        return apiService.callCheckBirthdayApi(request)
    }

    override suspend fun callInductionTrainingApi(request: CommonRequestModel): InductionTrainingResponseModel {
        return  apiService.callInductionTrainingApi(request)
    }

    override suspend fun callSurveyLinkApi(request: SurveyLinkRequestModel): SurveyLinkResponseModel {
        return  apiService.callSurveyLinkApi(request)
    }
}