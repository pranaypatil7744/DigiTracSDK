package com.example.digitracksdk.presentation.home.home_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.home_model.InductionTrainingResponseModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkRequestModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkResponseModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardResponseModel
import com.example.digitracksdk.domain.model.home_model.response.CheckBirthdayResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.dashboard.CheckBirthdayUseCase
import com.example.digitracksdk.domain.usecase.dashboard.InductionTrainingUseCase
import com.example.digitracksdk.domain.usecase.dashboard.SurveyLinkUseCase
import com.example.digitracksdk.domain.usecase.home_usecase.HomeDashboardUseCase
import kotlinx.coroutines.cancel

class HomeDashboardViewModel(
    private val homeDashboardUseCase: HomeDashboardUseCase,
    private val checkBirthdayUseCase: CheckBirthdayUseCase,
    private val inductionTrainingUseCase: InductionTrainingUseCase,
    private val surveyLinkUseCase : SurveyLinkUseCase

) : ViewModel() {

    val responseData = MutableLiveData<HomeDashboardResponseModel>()
    val birthdayResponseData = MutableLiveData<CheckBirthdayResponseModel>()
    val inductionTrainingResponseData = MutableLiveData<InductionTrainingResponseModel>()
    val surveyLinkResponseData  = MutableLiveData<SurveyLinkResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callHomeDashboardApi(request: HomeDashboardRequestModel) {
        showProgressbar.value = true
        homeDashboardUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<HomeDashboardResponseModel> {
                override fun onSuccess(result: HomeDashboardResponseModel) {
                    responseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }

            })
    }

    fun callCheckBirthdayApi(request: CommonRequestModel) {
        showProgressbar.value = true
        checkBirthdayUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CheckBirthdayResponseModel> {
                override fun onSuccess(result: CheckBirthdayResponseModel) {
                    birthdayResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }

            })
    }

    fun callInductionTrainingApi(request: CommonRequestModel) {
        showProgressbar.value = true
        inductionTrainingUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InductionTrainingResponseModel> {
                override fun onSuccess(result: InductionTrainingResponseModel) {
                    showProgressbar.value = false
                    inductionTrainingResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage().toString()
                }

            }

        )

    }

    fun callSurveyLinkApi(request  : SurveyLinkRequestModel)
    {
        showProgressbar.value = true
        surveyLinkUseCase.invoke(
            viewModelScope,
            request,
            object  : UseCaseResponse<SurveyLinkResponseModel> {
                override fun onSuccess(result: SurveyLinkResponseModel) {
                    showProgressbar.value = false
                    surveyLinkResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage().toString()
                }
            }
        )
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}