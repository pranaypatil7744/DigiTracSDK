package com.example.digitracksdk.domain.repository.training_repository

import com.example.digitracksdk.domain.model.training_model.TrainingRequestModel
import com.example.digitracksdk.domain.model.training_model.TrainingResponseModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentRequestModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentResponseModel

interface TrainingRepository {

    suspend fun callTrainingApi(request: TrainingRequestModel): TrainingResponseModel

    suspend fun callViewTrainingDocumentApi(request: ViewTrainingDocumentRequestModel): ViewTrainingDocumentResponseModel

}