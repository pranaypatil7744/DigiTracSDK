package com.example.digitracksdk.domain.usecase.onboarding.documents

import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentResponseModel
import com.example.digitracksdk.domain.repository.onboarding.documents.DocumentsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetUploadedDocumentUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<GetDocumentResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetDocumentResponseModel {
        return documentsRepository.callGetDocumentApi(params as GetDocumentRequestModel)
    }
}