package com.example.digitracksdk.presentation.onboarding.esic_details

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEsicResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertESICDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEsicResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewEsicUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.POBEsicUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,13:04
 */
class PaperlessViewEsicViewModel
constructor(
    private val viewEsicUseCase: PaperlessViewEsicUseCase,
    private val insertEsicUseCase: POBEsicUseCase

) : ViewModel() {

    val viewEsicResponseData = MutableLiveData<PaperlessViewEsicResponseModel>()
    val insertEsicResponseData = MutableLiveData<POBInsertEsicResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()
    var validationListener: ValidationListener? = null
    /**
     *  For Validation
     */

    fun validateESICRequestModel(request: POBInsertESICDetailsModel) {
        if (TextUtils.isEmpty(request.DoYouHaveAnOldESICNo)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ESIC_CHECK_ERROR,
                R.string.please_select_do_you_have_old_esic
            )
            return
        }
        if (TextUtils.isEmpty(request.ESICNo)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ESIC_NO_ERROR,
                R.string.please_enter_esic_number
            )
            return
        }
        if (TextUtils.isEmpty(request.InsuranceNo)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.INSURANCE_NO_ERROR,
                R.string.please_enter_insurance_number
            )
            return
        }
        if (AppUtils.INSTANCE?.checkSpecialSymbol(request.InsuranceNo) == true){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.INSURANCE_NO_ERROR,
                R.string.special_symbols_not_allowed_here
            )
            return
        }
        if (TextUtils.isEmpty(request.EmpCode)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EMP_CODE_ERROR,
                R.string.please_enter_emp_code
            )
            return
        }
        if (AppUtils.INSTANCE?.checkSpecialSymbol(request.EmpCode) == true){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EMP_CODE_ERROR,
                R.string.special_symbols_not_allowed_here
            )
            return
        }
        if (TextUtils.isEmpty(request.BranchOfficeName)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.BRANCH_OFFICE_ERROR,
                R.string.please_enter_branch_office
            )
            return
        }
        if (AppUtils.INSTANCE?.checkSpecialSymbol(request.BranchOfficeName) == true){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.BRANCH_OFFICE_ERROR,
                R.string.special_symbols_not_allowed_here
            )
            return
        }
        if (TextUtils.isEmpty(request.DispensaryName)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.DISPENSARY_NAME_ERROR,
                R.string.please_enter_dispensary_name
            )
            return
        }
        if (AppUtils.INSTANCE?.checkSpecialSymbol(request.DispensaryName) == true){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.DISPENSARY_NAME_ERROR,
                R.string.special_symbols_not_allowed_here
            )
            return
        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }

    /**
     *  For Insert Esic
     */

    fun callInsertEsicApi(request: POBInsertESICDetailsModel) {
        showProgressBar.value = true
        insertEsicUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<POBInsertEsicResponseModel> {
                override fun onSuccess(result: POBInsertEsicResponseModel) {
                    showProgressBar.value = false
                    insertEsicResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    fun callViewEsicApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewEsicUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewEsicResponseModel> {
                override fun onSuccess(result: PaperlessViewEsicResponseModel) {
                    showProgressBar.value = false
                    viewEsicResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }
}