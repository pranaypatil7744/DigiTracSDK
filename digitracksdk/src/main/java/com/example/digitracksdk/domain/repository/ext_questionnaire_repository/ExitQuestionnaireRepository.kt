package com.example.digitracksdk.domain.repository.ext_questionnaire_repository

import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireRequestModel
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireResponseModel

interface ExitQuestionnaireRepository {

    suspend fun callExitQuestionnaireApi(request : ExitQuestionnaireRequestModel) : ExitQuestionnaireResponseModel
}