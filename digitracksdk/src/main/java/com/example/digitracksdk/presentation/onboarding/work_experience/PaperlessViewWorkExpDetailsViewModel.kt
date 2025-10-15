package com.example.digitracksdk.presentation.onboarding.work_experience

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewWorkExpResponseModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpRequestModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewWorkExpDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.work_exp_usecase.InsertWorkExpUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils
import kotlinx.coroutines.cancel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,13:04
 */
class PaperlessViewWorkExpDetailsViewModel
constructor(
    private val viewWorkExpDetailsUseCase: PaperlessViewWorkExpDetailsUseCase,
    private val insertWorkExpUseCase: InsertWorkExpUseCase
) : ViewModel() {

    val viewWorkExpDetailsResponseData = MutableLiveData<PaperlessViewWorkExpResponseModel>()
    val insertWorkExpResponseData = MutableLiveData<InsertWorkExpResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    fun callViewWorkExpDetailsApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewWorkExpDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewWorkExpResponseModel> {
                override fun onSuccess(result: PaperlessViewWorkExpResponseModel) {
                    showProgressBar.value = false
                    viewWorkExpDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callInsertWorkExpApi(request: InsertWorkExpRequestModel) {
        showProgressBar.value = true
        insertWorkExpUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertWorkExpResponseModel> {
                override fun onSuccess(result: InsertWorkExpResponseModel) {
                    showProgressBar.value = false
                    insertWorkExpResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }
    fun validateWorkExpRequestModel(request: InsertWorkExpRequestModel, isFirstTime:Boolean) {
        if (request.isFresher == "null" || request.isFresher.isEmpty()) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.IS_FRESHER_ERROR,
                R.string.please_select_is_fresher
            )
            return
        }
        if (request.isFresher != "True"){
            if (isFirstTime){
                if (TextUtils.isEmpty(request.TotalExpInYear)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.TOTAL_YEAR_ERROR,
                        R.string.please_enter_total_exp_year
                    )
                    return
                }
                if (TextUtils.isEmpty(request.TotalExpMonth)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.TOTAL_MONTH_ERROR,
                        R.string.please_enter_total_exp_month
                    )
                    return
                }
                if (request.TotalExpMonth.toInt() > 12) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.TOTAL_MONTH_ERROR,
                        R.string.please_enter_valid_total_exp_month
                    )
                    return
                }
                if (TextUtils.isEmpty(request.totalRelevantExpYear)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.RELEVANT_YEAR_ERROR,
                        R.string.please_enter_relevant_exp_year
                    )
                    return
                }
                if (TextUtils.isEmpty(request.totalRelevantExpMonth)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.RELEVANT_MONTH_ERROR,
                        R.string.please_enter_relevant_exp_month
                    )
                    return
                }
                if (request.totalRelevantExpMonth.toInt() > 12) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.RELEVANT_MONTH_ERROR,
                        R.string.please_enter_valid_relevant_exp_month
                    )
                    return
                }
                val totalExp = request.TotalExpInYear+"."+request.TotalExpMonth
                val relevantExp = request.totalRelevantExpYear+"."+request.totalRelevantExpMonth
                if (totalExp.toFloat() < relevantExp.toFloat()){
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.RELEVANT_EXP_ERROR,
                        R.string.relevant_experience_cannot_be_greater_than_total_experience
                    )
                    return
                }
            }

            if (request.CurrentlyEmployed == "null" || request.CurrentlyEmployed.isEmpty()) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.CURRENTLY_EMPLOYED_ERROR,
                    R.string.please_select_currently_employed
                )
                return
            }
            if (TextUtils.isEmpty(request.CompanyName)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.COMPANY_NAME_ERROR,
                    R.string.please_enter_company_name
                )
                return
            }
            if (AppUtils.INSTANCE?.checkSpecialSymbol(request.CompanyName) == true){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.COMPANY_NAME_ERROR,
                    R.string.special_symbols_not_allowed_here
                )
                return
            }

            if (TextUtils.isEmpty(request.DateOfJoining)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.JOINING_DATE_ERROR,
                    R.string.please_choose_date_of_joining
                )
                return
            }
            if (request.CurrentlyEmployed != "Yes"){
                if (TextUtils.isEmpty(request.LastWorkingDate)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.LAST_WORKING_DATE_ERROR,
                        R.string.please_choose_last_working_date
                    )
                    return
                }
            }
         if (TextUtils.isEmpty(request.LastCTC)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.LAST_CTC_ERROR,
                    R.string.please_enter_last_ctc
                )
                return
            }
            if ((request.LastCTC.toLongOrNull() ?: 0) < 100000) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.LAST_CTC_ERROR,
                    R.string.please_enter_valid_last_ctc
                )
                return
            }
            if (TextUtils.isEmpty(request.Designation)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.DESIGNATION_ERROR,
                    R.string.please_enter_designation
                )
                return
            }
            if (AppUtils.INSTANCE?.checkSpecialSymbol(request.Designation) == true){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.DESIGNATION_ERROR,
                    R.string.special_symbols_not_allowed_here
                )
                return
            }
        }else{
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.IS_FRESHER_ERROR,
                R.string.you_are_fresher
            )
            return
        }

        validationListener?.onValidationSuccess(
            Constant.SUCCESS, R.string.success
        )
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}