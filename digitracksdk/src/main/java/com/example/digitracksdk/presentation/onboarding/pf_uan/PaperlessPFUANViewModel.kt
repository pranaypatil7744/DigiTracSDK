package com.example.digitracksdk.presentation.onboarding.pf_uan

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICResponseModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.pf_uan.GetPFESICUseCase
import com.example.digitracksdk.domain.usecase.onboarding.pf_uan.InsertPFESICUseCase
import com.example.digitracksdk.listener.ValidationListener
import kotlinx.coroutines.cancel

/**
 * Created by Mo Khurseed Ansari on 18-10-2023.
 */
class PaperlessPFUANViewModel
constructor(
    private val insertPFESICUseCase: InsertPFESICUseCase,
    private val getPFESICUseCase: GetPFESICUseCase

) : ViewModel() {
    val insertPFESICResponseData = MutableLiveData<InsertPFESICResponseModel>()
    val getPFESICResponseData = MutableLiveData<GetPFESICResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()
    var validationListener: ValidationListener? = null

    fun callInsertPFESICApi(request: InsertPFESICRequestModel) {
        showProgressBar.value = true
        insertPFESICUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertPFESICResponseModel> {
                override fun onSuccess(result: InsertPFESICResponseModel) {
                    showProgressBar.value = false
                    insertPFESICResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            }

        )
    }

    fun callGetPFESICApi(request: GetPFESICRequestModel) {
        showProgressBar.value = true
        getPFESICUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetPFESICResponseModel> {
                override fun onSuccess(result: GetPFESICResponseModel) {
                    showProgressBar.value = false
                    getPFESICResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            }

        )
    }

    fun validatePFESICRequestModel(appContext: Context, request: InsertPFESICRequestModel) {

        if (request.IsPFUAN.toString() == appContext.getString(R.string.yes)) {
            if (TextUtils.isEmpty(request.PFUANNumber)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.PF_UAN_NO_ERROR,
                    R.string.please_enter_uan_number
                )
                return
            }

        }

        if (request.IsESICIp.toString() == appContext.getString(R.string.yes)) {
            if (TextUtils.isEmpty(request.IsESICIpNumber)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.ESIC_NO_ERROR,
                    R.string.please_enter_esic_number
                )
                return
            }
            if (request.IsESICIpNumber.toString().length != 17) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.ESIC_CHECK_ERROR,
                    R.string.invalid_esic_number
                )
                return
            }

        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)

    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()

    }

}