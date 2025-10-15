package com.example.digitracksdk.domain.usecase.onboarding.documents

import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListResponseModel
import com.example.digitracksdk.domain.repository.onboarding.documents.DocumentsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PendingDocumentListUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<PendingDocumentsListResponseModel, Any?>() {
    override suspend fun run(params: Any?): PendingDocumentsListResponseModel {
        return documentsRepository.callGetPendingDocumentsListApi(params as PendingDocumentsListRequestModel)
    }
}