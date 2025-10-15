package com.example.digitracksdk.domain.usecase.onboarding.documents

import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.ViewDocumentsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.documents.DocumentsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewDocumentsListUseCase constructor(private val viewDocumentsRepository: DocumentsRepository):
    UseCase<ViewDocumentsResponseModel, Any?>() {
    override suspend fun run(params: Any?): ViewDocumentsResponseModel {
        return viewDocumentsRepository.callViewDocumentsListApi(params as EmpIDRequestModel)
    }
}