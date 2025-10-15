package com.example.digitracksdk.presentation.home.innov_id_card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeRequestModel
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeResponseModel
import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.dashboard.InnovIDCardUseCase
import com.example.digitracksdk.domain.usecase.dashboard.QrCodeUseCase
import kotlinx.coroutines.cancel

class InnovIDCardViewModel constructor(
    private val innovIDCardUseCase: InnovIDCardUseCase,
    private val qrCodeUseCase: QrCodeUseCase

) : ViewModel() {

    val innovIDCardResponseData = MutableLiveData<InnovIDCardResponseModel>()
    val qrCodeResponseData = MutableLiveData<QrCodeResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callInnovIDCardApi(request: InnovIDCardRequestModel) {
        showProgressbar.value = true
        innovIDCardUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InnovIDCardResponseModel> {
                override fun onSuccess(result: InnovIDCardResponseModel) {
                    innovIDCardResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }

            })
    }

    fun callQrCodeApi(request: QrCodeRequestModel) {
        showProgressbar.value = true
        qrCodeUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<QrCodeResponseModel> {
                override fun onSuccess(result: QrCodeResponseModel) {
                    qrCodeResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }

            })


    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    companion object {
        private val TAG = InnovIDCardViewModel::class.java.name
    }
}