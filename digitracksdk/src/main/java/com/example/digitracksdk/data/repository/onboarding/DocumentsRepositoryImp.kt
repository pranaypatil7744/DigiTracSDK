package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsResponseModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.ViewDocumentsResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.documents.DocumentsRepository

class DocumentsRepositoryImp
    (private val apiService: ApiService) : DocumentsRepository {
    override suspend fun callViewDocumentsListApi(request: EmpIDRequestModel): ViewDocumentsResponseModel {
        return apiService.callViewDocumentsListApi(request)
    }

    override suspend fun callGetPendingDocumentsListApi(request: PendingDocumentsListRequestModel): PendingDocumentsListResponseModel {
        return apiService.callGetPendingDocumentsListApi(request)
    }

    override suspend fun callInsertPendingDocumentsFileApi(request: InsertPendingDocumentsRequestModel): InsertPendingDocumentsResponseModel {
        return apiService.callInsertPendingDocumentsFileApi(request)
    }

    override suspend fun callUploadedDocumentsListApi(request: UploadedDocumentsRequestModel): UploadedDocumentsResponseModel {
        return apiService.callGetUploadedDocumentsListApi(request)
    }

    override suspend fun callGetDocumentApi(request: GetDocumentRequestModel): GetDocumentResponseModel {
        return apiService.callGetUploadedDocument(request)
    }

}