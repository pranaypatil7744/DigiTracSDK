package com.example.digitracksdk.domain.repository.home_repository

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.home_model.InductionTrainingResponseModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkRequestModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkResponseModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardResponseModel
import com.example.digitracksdk.domain.model.home_model.response.CheckBirthdayResponseModel

interface HomeRepository {

    suspend fun callHomeDashboardApi(request: HomeDashboardRequestModel): HomeDashboardResponseModel

    suspend fun callCheckBirthdayApi(request: CommonRequestModel): CheckBirthdayResponseModel

    suspend fun callInductionTrainingApi(request: CommonRequestModel) : InductionTrainingResponseModel

    suspend fun callSurveyLinkApi(request : SurveyLinkRequestModel) : SurveyLinkResponseModel
}