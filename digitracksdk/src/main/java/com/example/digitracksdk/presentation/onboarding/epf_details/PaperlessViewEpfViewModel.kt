package com.example.digitracksdk.presentation.onboarding.epf_details

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEpfResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewEpfUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.POBEpfUseCase
import com.example.digitracksdk.listener.ValidationListener


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,13:04
 */
class PaperlessViewEpfViewModel
constructor(
    private val viewEpfUseCase: PaperlessViewEpfUseCase,
    private val insertEpfUseCase: POBEpfUseCase

) : ViewModel() {

    val viewEpfResponseData = MutableLiveData<PaperlessViewEpfResponseModel>()
    val insertEpfResponseData = MutableLiveData<POBInsertEpfResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    fun validateEPFRequestModel(request: POBInsertEpfModel) {
        if (TextUtils.isEmpty(request.EmployeesPfCheck)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EPF_ERROR,
                R.string.please_select_are_you_eligible_for_epf
            )
            return
        }
        if (TextUtils.isEmpty(request.EmployeesPensionSchemeCheck1952)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EPF_PROVIDED_FUND_ERROR,
                R.string.please_select_earlier_member_of_emp_provided_fund
            )
            return
        }
        if (TextUtils.isEmpty(request.EmployeesPensionSchemeCheck)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EPF_SCHEME_ERROR,
                R.string.please_select_earlier_member_of_pension_scheme
            )
            return
        }
        if (TextUtils.isEmpty(request.ExistUANNo)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.UAN_NO_ERROR,
                R.string.please_enter_uan_number
            )
            return
        }
        if (TextUtils.isEmpty(request.ExistPFNo)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.PF_MEMBER_ID_ERROR,
                R.string.please_enter_prev_member_id
            )
            return
        }
        if (TextUtils.isEmpty(request.ExitDate)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXIT_DATE_ERROR,
                R.string.please_choose_exit_date
            )
            return
        }
        validationListener?.onValidationSuccess(Constant.success,R.string.success)
    }

    //// For Insert Epf


    fun callInsertEpfApi(request: POBInsertEpfModel) {
        showProgressBar.value = true
        insertEpfUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<POBInsertEpfResponseModel> {
                override fun onSuccess(result: POBInsertEpfResponseModel) {
                    showProgressBar.value = false
                    insertEpfResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }


/// for View Epf
    fun callViewEpfApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewEpfUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewEpfResponseModel> {
                override fun onSuccess(result: PaperlessViewEpfResponseModel) {
                    showProgressBar.value = false
                    viewEpfResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }
}