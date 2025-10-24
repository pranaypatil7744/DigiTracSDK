package com.example.digitracksdk.presentation.onboarding.cibil_score

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreRequestModel
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.cibil_score.GetCibilScoreUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils


class CibilScoreViewModel constructor(private val cibilScoreUseCase: GetCibilScoreUseCase) :
    ViewModel() {
    var listener: ValidationListener? = null
    val cibilScoreResponseModel = MutableLiveData<GetCibilScoreResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()


    fun verifyCibilDetails(request: GetCibilScoreRequestModel) {
        if (AppUtils.INSTANCE?.isValidPanCard(request.PanCard) == false) {
            listener?.onValidationFailure(
                Constant.ListenerConstants.PAN_CARD_ERROR,
                R.string.please_enter_valid_pan_number
            )
            return
        }

        listener?.onValidationSuccess(Constant.SUCCESS, R.string.success)
    }


    fun callGetCibilScoreApi(request: GetCibilScoreRequestModel){
        showProgressBar.value = true
        cibilScoreUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<GetCibilScoreResponseModel> {
            override fun onSuccess(result: GetCibilScoreResponseModel) {
                showProgressBar.value = false
                cibilScoreResponseModel.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }

}