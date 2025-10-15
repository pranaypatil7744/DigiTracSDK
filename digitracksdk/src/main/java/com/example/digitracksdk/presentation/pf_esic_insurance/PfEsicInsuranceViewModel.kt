package com.example.digitracksdk.presentation.pf_esic_insurance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.pf_esic_insurance_usecase.EsicCardUseCase
import com.example.digitracksdk.domain.usecase.pf_esic_insurance_usecase.EsicMedicalCardUseCase
import com.example.digitracksdk.domain.usecase.pf_esic_insurance_usecase.MedicalCardUseCase
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicRequestModel
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicResponseModel
import kotlinx.coroutines.cancel

class PfEsicInsuranceViewModel(
    private val esicMedicalCardUseCase: EsicMedicalCardUseCase,
    private val esicCardUseCase: EsicCardUseCase,
    private val medicalCardUseCase: MedicalCardUseCase
) : ViewModel() {
    val responseData = MutableLiveData<PfEsicResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callEsicMedicalCardApi(request: PfEsicRequestModel) {
        showProgressbar.value = true
        esicMedicalCardUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PfEsicResponseModel> {
                override fun onSuccess(result: PfEsicResponseModel) {
                    responseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

    fun callEsicCardApi(request: PfEsicRequestModel) {
        showProgressbar.value = true
        esicCardUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PfEsicResponseModel> {
                override fun onSuccess(result: PfEsicResponseModel) {
                    responseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

    fun callMedicalCardApi(request: PfEsicRequestModel) {
        showProgressbar.value = true
        medicalCardUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PfEsicResponseModel> {
                override fun onSuccess(result: PfEsicResponseModel) {
                    responseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}