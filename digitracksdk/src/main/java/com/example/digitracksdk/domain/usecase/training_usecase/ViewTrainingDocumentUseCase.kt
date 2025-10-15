package com.example.digitracksdk.domain.usecase.training_usecase

import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentRequestModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentResponseModel
import com.example.digitracksdk.domain.repository.training_repository.TrainingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewTrainingDocumentUseCase constructor(private val trainingRepository: TrainingRepository) :
    UseCase<ViewTrainingDocumentResponseModel, Any?>() {
    override suspend fun run(params: Any?): ViewTrainingDocumentResponseModel {
        return trainingRepository.callViewTrainingDocumentApi(params as ViewTrainingDocumentRequestModel)
    }
}