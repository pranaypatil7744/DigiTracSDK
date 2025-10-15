package com.example.digitracksdk.presentation.onboarding.signature_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetSignatureResponseModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureRequestModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.signature_usecase.InsertSignatureUseCase
import com.example.digitracksdk.domain.usecase.onboarding.signature_usecase.PaperlessViewGetSignatureUseCase
import kotlinx.coroutines.cancel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,13:04
 */
class PaperlessViewGetSignatureViewModel
constructor(
    private val viewGetSignatureUseCase: PaperlessViewGetSignatureUseCase,
    private val insertSignatureUseCase: InsertSignatureUseCase

) : ViewModel() {

    val viewGetSignatureResponseData = MutableLiveData<PaperlessViewGetSignatureResponseModel>()
    val insertSignatureResponseData = MutableLiveData<InsertSignatureResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callInsertSignatureApi(request: InsertSignatureRequestModel) {
        showProgressBar.value = true
        insertSignatureUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertSignatureResponseModel> {
                override fun onSuccess(result: InsertSignatureResponseModel) {
                    showProgressBar.value = false
                    insertSignatureResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    fun callViewGetSignatureApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewGetSignatureUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewGetSignatureResponseModel> {
                override fun onSuccess(result: PaperlessViewGetSignatureResponseModel) {
                    showProgressBar.value = false
                    viewGetSignatureResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}