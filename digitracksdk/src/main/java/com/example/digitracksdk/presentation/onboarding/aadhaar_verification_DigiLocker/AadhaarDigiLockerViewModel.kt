package com.example.digitracksdk.presentation.onboarding.aadhaar_verification_DigiLocker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker.AadhaarDataUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker.AadhaarSaveRequestIdUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker.AadhaarVerificationDigiLockerUseCase
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerRequestModel
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDResponseModel

class AadhaarDigiLockerViewModel(
    val aadhaarDigiLockerUseCase: AadhaarVerificationDigiLockerUseCase,
    val saveRequestIdUseCase: AadhaarSaveRequestIdUseCase,
    val aadhaarDataUseCase: AadhaarDataUseCase

) : ViewModel() {

    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    val aadhaarDigiLockerData = MutableLiveData<DigiLockerResponseModel>()
    val saveDigiLockerRequestIdData = MutableLiveData<SaveDigiLockerRequestIDResponseModel>()
    val aadhaarData = MutableLiveData<GetAadhaarDetailDigiLockerResponseModel>()

    fun callAadhaarDigiLockerApi(request: DigiLockerRequestModel) {
        showProgressBar.value = true
        aadhaarDigiLockerUseCase.invoke(viewModelScope, request, object :
            UseCaseResponse<DigiLockerResponseModel> {
            override fun onSuccess(result: DigiLockerResponseModel) {
                showProgressBar.value = false
                aadhaarDigiLockerData.value = result

            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })

    }

    fun callSaveRequestIdApi(request: SaveDigiLockerRequestIDRequestModel) {
        showProgressBar.value = true
        saveRequestIdUseCase.invoke(viewModelScope, request, object :
            UseCaseResponse<SaveDigiLockerRequestIDResponseModel> {
            override fun onSuccess(result: SaveDigiLockerRequestIDResponseModel) {
                showProgressBar.value = false
                saveDigiLockerRequestIdData.value = result

            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }


        })

    }

    fun callGetAadhaarDataApi(request: GetAadhaarDetailDigiLockerRequestModel) {
        showProgressBar.value = true
        aadhaarDataUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetAadhaarDetailDigiLockerResponseModel> {
                override fun onSuccess(result: GetAadhaarDetailDigiLockerResponseModel) {
                    showProgressBar.value = false
                    aadhaarData.value = result

                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })


    }
}