package com.example.digitracksdk.presentation.onboarding.pan_verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.pan_verification.PanVerificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.pan_verification.PanVerificationUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils

class PanVerificationViewModel constructor(
    private val panVerificationUseCase: PanVerificationUseCase,
    private val panVerificationDetailsUseCase: PanVerificationDetailsUseCase

) :
    ViewModel() {
    var listener: ValidationListener? = null
    val panVerificationResponseData = MutableLiveData<PanCardVerificationResponseModel>()
    val panVerificationDetailsResponseData =
        MutableLiveData<GetPanCardVerificationDetailsResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()


    fun verifyPanDetails(request: PanCardVerificationRequestModel) {

        if (AppUtils.INSTANCE?.isValidPanCard(request.PANNumber) == false) {
            listener?.onValidationFailure(
                Constant.ListenerConstants.PAN_CARD_ERROR,
                R.string.please_enter_valid_pan_number
            )
            return
        }

        listener?.onValidationSuccess(Constant.SUCCESS, R.string.success)
    }


    fun callPanVerificationApi(request: PanCardVerificationRequestModel) {
        showProgressBar.value = true
        panVerificationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PanCardVerificationResponseModel> {
                override fun onSuccess(result: PanCardVerificationResponseModel) {
                    showProgressBar.value = false
                    panVerificationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callPanVerificationDetailsApi(request: GetPanCardVerificationDetailsRequestModel) {
        showProgressBar.value = true
        panVerificationDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetPanCardVerificationDetailsResponseModel> {
                override fun onSuccess(result: GetPanCardVerificationDetailsResponseModel) {
                    showProgressBar.value = false
                    panVerificationDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            }
        )
    }

}