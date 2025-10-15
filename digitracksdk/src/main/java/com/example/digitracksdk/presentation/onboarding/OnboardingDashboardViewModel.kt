package com.example.digitracksdk.presentation.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.OnboardingDashboardResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.OnboardingDashboardUseCase

class OnboardingDashboardViewModel constructor(private val onBoardingDashboardUseCase: OnboardingDashboardUseCase) :
    ViewModel() {

    val onBoardingDashboardResponseData = MutableLiveData<OnboardingDashboardResponseModel>()
    val messageData = MutableLiveData<String>()
    val showProgressBar = MutableLiveData<Boolean>()

    fun callOnBoardingDashboardApi(request: CommonRequestModel){
        showProgressBar.value = true
        onBoardingDashboardUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<OnboardingDashboardResponseModel> {
            override fun onSuccess(result: OnboardingDashboardResponseModel) {
                showProgressBar.value = false
                onBoardingDashboardResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }
}