package com.example.digitracksdk.presentation.onboarding.aadhar_verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.GetAadhaarVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarResponseModel
import com.innov.digitrac.domain.model.onboarding.aadhaar_verification.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.AadhaarVerificationSendOtpUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.GetAadhaarVerificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.ValidateAadhaarUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils

class AadhaarVerificationViewModel(private val aadhaarVerificationSendOtpUseCase: AadhaarVerificationSendOtpUseCase,
                                   private val aadhaarVerificationOtpValidationUseCase: AadhaarVerificationOtpValidationUseCase,
                                   private val validateAadhaarUseCase: ValidateAadhaarUseCase,
                                   private val getAadhaarVerificationDetailsUseCase: GetAadhaarVerificationDetailsUseCase

) :
    ViewModel() {
    var listener: ValidationListener? = null
    val aadhaarVerificationSendOtpResponseData = MutableLiveData<AadhaarVerificationSendOtpResponseModel>()
    val aadhaarVerificationOtpValidationResponseData = MutableLiveData<AadhaarVerificationOtpValidationResponseModel>()
    val validateAadhaarResponseModel = MutableLiveData<ValidateAadhaarResponseModel>()
    val getAadhaarVerificationDetailsResponseModel = MutableLiveData<GetAadhaarVerificationDetailsResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()


    fun verifyAadhaarDetails(request: AadhaarVerificationSendOtpRequestModel) {
        if (request.AadhaarNumber.isEmpty()) {
            listener?.onValidationFailure(
                Constant.ListenerConstants.AADHAR_NUMBER_ERROR,
                R.string.please_enter_adhar_card_number
            )
            return
        }

        if (AppUtils.INSTANCE?.validateAadharNumber(request.AadhaarNumber) == false) {
            listener?.onValidationFailure(
                Constant.ListenerConstants.AADHAR_NUMBER_ERROR,
                R.string.please_enter_valid_adhar_card_number
            )
            return
        }

        listener?.onValidationSuccess(Constant.SUCCESS, R.string.success)
    }

    fun callAadhaarVerificationSendOtpApi(request: AadhaarVerificationSendOtpRequestModel){
        showProgressBar.value = true
        aadhaarVerificationSendOtpUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<AadhaarVerificationSendOtpResponseModel> {
            override fun onSuccess(result: AadhaarVerificationSendOtpResponseModel) {
                showProgressBar.value = false
                aadhaarVerificationSendOtpResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }

    fun callAadhaarVerificationOtpValidationApi(request: AadhaarVerificationOtpValidationRequestModel){
        showProgressBar.value = true
        aadhaarVerificationOtpValidationUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<AadhaarVerificationOtpValidationResponseModel> {
            override fun onSuccess(result: AadhaarVerificationOtpValidationResponseModel) {
                showProgressBar.value = false
                aadhaarVerificationOtpValidationResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }
    fun callValidateAadhaarApi(request: ValidateAadhaarRequestModel){
        showProgressBar.value = true
        validateAadhaarUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<ValidateAadhaarResponseModel> {
            override fun onSuccess(result: ValidateAadhaarResponseModel) {
                showProgressBar.value = false
                validateAadhaarResponseModel.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }
    fun callGetAadhaarVerificationDetailsApi(request: ValidateAadhaarRequestModel){
        showProgressBar.value = true
        getAadhaarVerificationDetailsUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<GetAadhaarVerificationDetailsResponseModel> {
            override fun onSuccess(result: GetAadhaarVerificationDetailsResponseModel) {
                showProgressBar.value = false
                getAadhaarVerificationDetailsResponseModel.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }

}