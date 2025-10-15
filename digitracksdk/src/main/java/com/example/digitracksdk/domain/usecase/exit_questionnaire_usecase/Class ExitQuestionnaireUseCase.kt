package com.example.digitracksdk.domain.usecase.exit_questionnaire_usecase

import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireRequestModel
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireResponseModel
import com.example.digitracksdk.domain.repository.ext_questionnaire_repository.ExitQuestionnaireRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class  ExitQuestionnaireUseCase (private val repository : ExitQuestionnaireRepository)
: UseCase<ExitQuestionnaireResponseModel, Any>()

{
    override suspend fun run(params: Any?): ExitQuestionnaireResponseModel {
        return repository.callExitQuestionnaireApi(params as ExitQuestionnaireRequestModel)
    }
}