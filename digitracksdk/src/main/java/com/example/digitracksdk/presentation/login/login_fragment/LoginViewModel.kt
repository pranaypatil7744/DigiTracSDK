package com.example.digitracksdk.presentation.login.login_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.login_model.CheckVersionResponseModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateResponseModel
import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.domain.model.login_model.LoginResponseModel
import com.example.digitracksdk.domain.usecase.login_usecase.LoginUseCase
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.login_usecase.CheckAppVersionUseCase
import com.example.digitracksdk.domain.usecase.login_usecase.FirebaseTokenUpdateUseCase
import kotlinx.coroutines.cancel

class LoginViewModel constructor(
    private val loginUseCase: LoginUseCase,
    private val firebaseTokenUpdateUseCase: FirebaseTokenUpdateUseCase,
    private val checkAppVersionUseCase: CheckAppVersionUseCase
) : ViewModel() {

    val loginResponseData = MutableLiveData<LoginResponseModel>()
    val firebaseTokenUpdateResponseData = MutableLiveData<FirebaseTokenUpdateResponseModel>()
    val checkAppVersionResponseData = MutableLiveData<CheckVersionResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callLoginApi(request: LoginRequestModel) {
        showProgressbar.value = true
        loginUseCase.invoke(viewModelScope, request, object : UseCaseResponse<LoginResponseModel> {
            override fun onSuccess(result: LoginResponseModel) {
                loginResponseData.value = result
                showProgressbar.value = false
            }

            override fun onError(apiError: ApiError?) {
                messageData.value = apiError?.message.toString()
                showProgressbar.value = false
            }

        })
    }

    fun callCheckAppVersionApi() {
        checkAppVersionUseCase.invoke(
            viewModelScope,
            null,
            object : UseCaseResponse<CheckVersionResponseModel> {
                override fun onSuccess(result: CheckVersionResponseModel) {
                    checkAppVersionResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callFirebaseTokenUpdateApi(request: FirebaseTokenUpdateRequestModel) {
        showProgressbar.value = true
        firebaseTokenUpdateUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<FirebaseTokenUpdateResponseModel> {
                override fun onSuccess(result: FirebaseTokenUpdateResponseModel) {
                    firebaseTokenUpdateResponseData.value = result
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
        private val TAG = LoginViewModel::class.java.name
    }
}