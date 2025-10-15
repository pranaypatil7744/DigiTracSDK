package com.example.digitracksdk.domain.repository.onboarding.documents

import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsResponseModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListResponseModel
import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.ViewDocumentsResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsResponseModel

interface DocumentsRepository {

    suspend fun callViewDocumentsListApi(request: EmpIDRequestModel): ViewDocumentsResponseModel

    suspend fun callGetPendingDocumentsListApi(request: PendingDocumentsListRequestModel): PendingDocumentsListResponseModel

    suspend fun callInsertPendingDocumentsFileApi(request: InsertPendingDocumentsRequestModel): InsertPendingDocumentsResponseModel

    suspend fun callUploadedDocumentsListApi(request: UploadedDocumentsRequestModel): UploadedDocumentsResponseModel

    suspend fun callGetDocumentApi(request: GetDocumentRequestModel): GetDocumentResponseModel
}

