package com.example.digitracksdk.data.repository.training_repo_imp

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.training_model.TrainingRequestModel
import com.example.digitracksdk.domain.model.training_model.TrainingResponseModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentRequestModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentResponseModel
import com.example.digitracksdk.domain.repository.training_repository.TrainingRepository

class TrainingRepositoryImp (private val apiService: ApiService): TrainingRepository {
    override suspend fun callTrainingApi(request: TrainingRequestModel): TrainingResponseModel {
        return apiService.callTrainingApi(request)
    }

    override suspend fun callViewTrainingDocumentApi(request: ViewTrainingDocumentRequestModel): ViewTrainingDocumentResponseModel {
        return apiService.callViewTrainingDocumentApi(request)
    }
}