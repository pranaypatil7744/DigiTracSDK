package com.example.digitracksdk.presentation.onboarding.document

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsResponseModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListResponseModel
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.ViewDocumentsResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.documents.GetUploadedDocumentUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.InsertPendingDocumentsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.PendingDocumentListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.UploadedDocumentsListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.ViewDocumentsListUseCase
import kotlinx.coroutines.cancel

class DocumentsDetailsViewModel constructor(
    private val viewDocumentsListUseCase: ViewDocumentsListUseCase,
    private val pendingDocumentListUseCase: PendingDocumentListUseCase,
    private val insertPendingDocumentsUseCase: InsertPendingDocumentsUseCase,
    private val getUploadedDocumentUseCase: GetUploadedDocumentUseCase,
    private val uploadedDocumentsListUseCase: UploadedDocumentsListUseCase
) : ViewModel() {
    var viewDocumentsListResponseData = MutableLiveData<ViewDocumentsResponseModel>()
    var pendingDocumentsListResponseData = MutableLiveData<PendingDocumentsListResponseModel>()
    var insertPendingDocumentsResponseData = MutableLiveData<InsertPendingDocumentsResponseModel>()
    var uploadedDocumentsListResponseData = MutableLiveData<UploadedDocumentsResponseModel>()
    var getUploadedDocumentResponseData = MutableLiveData<GetDocumentResponseModel>()
    var messageData = MutableLiveData<String>()

    fun callUploadedDocumentsListApi(request: UploadedDocumentsRequestModel) {
        uploadedDocumentsListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UploadedDocumentsResponseModel> {
                override fun onSuccess(result: UploadedDocumentsResponseModel) {
                    uploadedDocumentsListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGetUploadedDocumentApi(request: GetDocumentRequestModel) {
        getUploadedDocumentUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetDocumentResponseModel> {
                override fun onSuccess(result: GetDocumentResponseModel) {
                    getUploadedDocumentResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callViewDocumentsListApi(request: EmpIDRequestModel) {
        viewDocumentsListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ViewDocumentsResponseModel> {
                override fun onSuccess(result: ViewDocumentsResponseModel) {
                    viewDocumentsListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callInsertPendingDocumentsApi(request: InsertPendingDocumentsRequestModel) {
        insertPendingDocumentsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertPendingDocumentsResponseModel> {
                override fun onSuccess(result: InsertPendingDocumentsResponseModel) {
                    insertPendingDocumentsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callPendingDocumentsListApi(request: PendingDocumentsListRequestModel) {
        pendingDocumentListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PendingDocumentsListResponseModel> {
                override fun onSuccess(result: PendingDocumentsListResponseModel) {
                    pendingDocumentsListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}