package com.example.digitracksdk.data.repository.exit_questionnaire_repo_imp

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireRequestModel
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireResponseModel
import com.example.digitracksdk.domain.repository.ext_questionnaire_repository.ExitQuestionnaireRepository

class ExitQuestionnaireRepositoryImp
    (private val apiService: ApiService) : ExitQuestionnaireRepository {
    override suspend fun callExitQuestionnaireApi(request: ExitQuestionnaireRequestModel): ExitQuestionnaireResponseModel {

        return apiService.callInsetExitInterviewApi(request)
    }
}