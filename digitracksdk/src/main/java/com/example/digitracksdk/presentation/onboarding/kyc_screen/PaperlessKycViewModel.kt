package com.example.digitracksdk.presentation.onboarding.kyc_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsResponseModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentListResponseModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.documents.InsertPendingDocumentsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.kyc.KycDocumentPendingListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.kyc.KycDocumentsUseCase

class PaperlessKycViewModel
constructor(
    private val kycDocumentsUseCase: KycDocumentsUseCase,
    private val getDocumentTypeKycDocumentPendingListUseCase: KycDocumentPendingListUseCase,
    private val insertDocumentsFileSystemDTUseCase: InsertPendingDocumentsUseCase
) : ViewModel() {

    val kYCDocsResponseData = MutableLiveData<KycDocumentListResponseModel>()
    val getPendingDocumentTypeKycResponseData = MutableLiveData<KycDocumentPendingListResponseModel>()
    val insertDocumentsFileSystemDTResponseData =
        MutableLiveData<InsertPendingDocumentsResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    //// For Insert Document


    fun callInsertDocumentsFileSystemDTApi(request: InsertPendingDocumentsRequestModel) {
        showProgressBar.value = true
        insertDocumentsFileSystemDTUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertPendingDocumentsResponseModel> {
                override fun onSuccess(result: InsertPendingDocumentsResponseModel) {
                    showProgressBar.value = false
                    insertDocumentsFileSystemDTResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callDTGetKYCDocsApi(request: EmpIDRequestModel) {
        showProgressBar.value = true
        kycDocumentsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<KycDocumentListResponseModel> {
                override fun onSuccess(result: KycDocumentListResponseModel) {
                    showProgressBar.value = false
                    kYCDocsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }


    /// for View Epf
    fun callGetDocumentTypeKycApi(request: KycDocumentPendingListRequestModel) {
        showProgressBar.value = true
        getDocumentTypeKycDocumentPendingListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<KycDocumentPendingListResponseModel> {
                override fun onSuccess(result: KycDocumentPendingListResponseModel) {
                    showProgressBar.value = false
                    getPendingDocumentTypeKycResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }
}