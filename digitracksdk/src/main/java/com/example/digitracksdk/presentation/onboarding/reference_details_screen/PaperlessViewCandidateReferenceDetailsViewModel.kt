package com.example.digitracksdk.presentation.onboarding.reference_details_screen

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.GetReferenceCategoryModel
import com.example.digitracksdk.domain.model.onboarding.insert.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewCandidateReferenceDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.GetReferenceCategoryUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.InsertCandidateReferenceDetailsUseCase
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils
import kotlinx.coroutines.cancel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,13:04
 */
class PaperlessViewCandidateReferenceDetailsViewModel
constructor(
    private val viewCandidateReferenceDetailsUseCase: PaperlessViewCandidateReferenceDetailsUseCase,
    private val getReferenceCategoryUseCase: GetReferenceCategoryUseCase,
    private val insertCandidateReferenceDetailsUseCase: InsertCandidateReferenceDetailsUseCase


) : ViewModel() {

//    http://paperlessonboardinglive.innov.in/Api/GetReferenceCategory
    val viewCandidateReferenceDetailsResponseData =
        MutableLiveData<PaperlessViewGetCandidateReferenceDetailsResponseModel>()
    val getReferenceCategoryResponseData =
        MutableLiveData<GetReferenceCategoryModel>()

    val insertCandidateReferenceDetailsResponseData =
        MutableLiveData<InsertCandidateReferenceDetailsResponseModel>()

    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    /**
     *  for get reference Category
     */

    fun callGetReferenceCategorysApi() {
        showProgressBar.value = true
        getReferenceCategoryUseCase.invoke(

            viewModelScope,
            Any() ,
            object : UseCaseResponse<GetReferenceCategoryModel> {
                override fun onSuccess(result: GetReferenceCategoryModel) {
                    showProgressBar.value = false
                    getReferenceCategoryResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    var validationListener: ValidationListener? = null

    fun validateInsertCandidateReferenceDetailsModel(request: InsertCandidateReferenceDetailsModel, context: Context) {
        if (request.ReferenceCategory == context.getString(R.string.Select)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.REFERENCE_TYPE_ERROR,
                R.string.please_select_are_reference_category
            )
            return
        }
        if (TextUtils.isEmpty(request.Name)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.NAME_ERROR,
                R.string.please_enter_name
            )
            return
        }
        if (AppUtils.INSTANCE?.checkSpecialSymbol(request.Name) == true){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.NAME_ERROR,
                R.string.special_symbols_not_allowed_here
            )
            return
        }
        if (TextUtils.isEmpty(request.ContactNo)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.MOBILE_ERROR,
                R.string.please_enter_contact
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidMobileNumber(request.ContactNo) == false) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.MOBILE_ERROR,
                R.string.please_enter_valid_mobile_number
            )
            return
        }
        if (TextUtils.isEmpty(request.EmailId)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR,
                R.string.please_enter_email
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidEmail(request.EmailId) == false){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CANDIDATE_EMAIL_ERROR,
                R.string.please_enter_valid_email
            )
            return
        }
        if (TextUtils.isEmpty(request.Address)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.ADDRESS_1_ERROR,
                R.string.please_enter_address
            )
            return
        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }

    /**
     *  for insert
     */

    fun callInsertCandidateReferenceDetailsApi(request: InsertCandidateReferenceDetailsModel) {
        showProgressBar.value = true
        insertCandidateReferenceDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertCandidateReferenceDetailsResponseModel> {
                override fun onSuccess(result: InsertCandidateReferenceDetailsResponseModel) {
                    showProgressBar.value = false
                    insertCandidateReferenceDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callViewCandidateReferenceDetailsApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewCandidateReferenceDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewGetCandidateReferenceDetailsResponseModel> {
                override fun onSuccess(result: PaperlessViewGetCandidateReferenceDetailsResponseModel) {
                    showProgressBar.value = false
                    viewCandidateReferenceDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}