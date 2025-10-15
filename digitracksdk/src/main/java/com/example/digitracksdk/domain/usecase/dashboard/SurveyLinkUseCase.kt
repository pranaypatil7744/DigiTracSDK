package com.example.digitracksdk.domain.usecase.dashboard

import com.example.digitracksdk.domain.model.home_model.SurveyLinkRequestModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkResponseModel
import com.example.digitracksdk.domain.repository.home_repository.HomeRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class SurveyLinkUseCase(val repository: HomeRepository) : UseCase<SurveyLinkResponseModel, Any>() {
    override suspend fun run(params: Any?): SurveyLinkResponseModel {

        return repository.callSurveyLinkApi(params as SurveyLinkRequestModel)
    }
}