package com.example.digitracksdk.domain.usecase.training_usecase

import com.example.digitracksdk.domain.model.training_model.TrainingRequestModel
import com.example.digitracksdk.domain.model.training_model.TrainingResponseModel
import com.example.digitracksdk.domain.repository.training_repository.TrainingRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class TrainingUseCase constructor(private val trainingRepository: TrainingRepository) :
    UseCase<TrainingResponseModel, Any?>() {
    override suspend fun run(params: Any?): TrainingResponseModel {
        return trainingRepository.callTrainingApi(params as TrainingRequestModel)
    }
}