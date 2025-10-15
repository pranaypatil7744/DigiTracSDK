package com.example.digitracksdk.presentation.login.verify_otp_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyRequestModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.login_usecase.VerifyLoginOtpUseCase
import kotlinx.coroutines.cancel

class VerifyLoginOtpViewModel constructor(private val verifyLoginOtpUseCase: VerifyLoginOtpUseCase) :
    ViewModel() {
    val responseData = MutableLiveData<LoginOtpVerifyResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callVerifyLoginOtpApi(request: LoginOtpVerifyRequestModel){
        showProgressbar.value = true
        verifyLoginOtpUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<LoginOtpVerifyResponseModel> {
            override fun onSuccess(result: LoginOtpVerifyResponseModel) {
                responseData.value = result
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
}