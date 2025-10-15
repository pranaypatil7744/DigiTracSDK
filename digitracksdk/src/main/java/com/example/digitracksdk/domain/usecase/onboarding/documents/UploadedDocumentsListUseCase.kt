package com.example.digitracksdk.domain.usecase.onboarding.documents

import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.documents.DocumentsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UploadedDocumentsListUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<UploadedDocumentsResponseModel, Any?>() {
    override suspend fun run(params: Any?): UploadedDocumentsResponseModel {
        return documentsRepository.callUploadedDocumentsListApi(params as UploadedDocumentsRequestModel)
    }
}