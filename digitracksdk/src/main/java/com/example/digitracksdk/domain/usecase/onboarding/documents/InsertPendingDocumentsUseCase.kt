package com.example.digitracksdk.domain.usecase.onboarding.documents

import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.documents.DocumentsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertPendingDocumentsUseCase
constructor(private val repository: DocumentsRepository) :
    UseCase<InsertPendingDocumentsResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertPendingDocumentsResponseModel {
        return repository.callInsertPendingDocumentsFileApi(params as InsertPendingDocumentsRequestModel)
    }

}