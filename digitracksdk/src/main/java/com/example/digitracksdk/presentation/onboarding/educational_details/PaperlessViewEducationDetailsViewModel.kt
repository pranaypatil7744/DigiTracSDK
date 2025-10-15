package com.example.digitracksdk.presentation.onboarding.educational_details

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.onboarding.GetEducationCategoryResponseModel
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEducationDetailsResponseModel
import com.innov.digitrac.domain.model.onboarding.*
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamRequestModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.onboarding.GetEducationCategoryUseCase
import com.example.digitracksdk.domain.usecase.onboarding.education_details.EducationalStreamUseCase
import com.example.digitracksdk.domain.usecase.onboarding.education_details.PaperlessViewEducationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.POBInsertEducationInfoUseCase
import com.example.digitracksdk.listener.ValidationListener
import kotlinx.coroutines.cancel

class PaperlessViewEducationDetailsViewModel
constructor(
    private val viewEducationDetailsUseCase: PaperlessViewEducationDetailsUseCase,
    private val getEducationCategoryUseCase: GetEducationCategoryUseCase,
    private val getPOBInsertEducationInfoUseCase: POBInsertEducationInfoUseCase,
    private val getEducationalStreamUseCase: EducationalStreamUseCase

) : ViewModel() {

    val viewEducationDetailsResponseData =
        MutableLiveData<PaperlessViewEducationDetailsResponseModel>()
    val getEducationCategoryResponseData = MutableLiveData<GetEducationCategoryResponseModel>()
    val getPOBInsertEducationInfoResponseData = MutableLiveData<InsertEducationInfoResponseModel>()
    val getEducationalStreamResponseData = MutableLiveData<EducationalStreamResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    /**
     *  For Validation
     */

    fun validatePOBInsertEducationInfoModel(
        appContext: Context,
        request: InsertEducationInfoRequestModel
    ) {

        if (TextUtils.isEmpty(request.EducationCategoryName)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EDUCATION_CATEGORY_NAME_ERROR,
                R.string.please_select_the_highest_education
            )
            return
        }
        if (request.EducationCategoryName != "Illiterate") {

            if (TextUtils.isEmpty(request.QualificationType)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATIONAL_STREAM_NAME_ERROR,
                    R.string.please_select_are_education_category

                )
                return
            }
            if (request.QualificationType.lowercase() == appContext.getString(R.string.select)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATIONAL_STREAM_ERROR,
                    R.string.please_select_are_education_category
                )
                return
            }

            if (TextUtils.isEmpty(request.PassYear)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATION_PASSING_YEAR_ERROR,
                    R.string.please_enter_passing_Year
                )
                return
            }
            if (TextUtils.isEmpty(request.BoardName)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATION_BOARD_NAME_ERROR,
                    R.string.please_enter_board_name
                )
                return
            }
            if (request.BoardName.length < 3) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATION_BOARD_NAME_ERROR,
                    R.string.board_name_should_have_atleast_3_character
                )
                return
            }
            if (TextUtils.isEmpty(request.InstituteName)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATION_SCHOOL_NAME_ERROR,
                    R.string.please_enter_school_name
                )
                return
            }
            if (request.InstituteName.length < 3) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATION_SCHOOL_NAME_ERROR,
                    R.string.institute_name_should_have_atleast_3_character
                )
                return
            }
            if (TextUtils.isEmpty(request.Percentage)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATION_MARKS_OBTAINED_ERROR,
                    R.string.please_enter_Marks_obtained
                )
                return
            }
            if ((request.Percentage.toIntOrNull() ?: 0) > 100) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.EDUCATION_MARKS_OBTAINED_ERROR,
                    R.string.please_enter_valid_percentage
                )
                return
            }
        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }

    fun callViewEducationDetailsApi(request: InnovIDRequestModel) {
        showProgressBar.value = true
        viewEducationDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PaperlessViewEducationDetailsResponseModel> {
                override fun onSuccess(result: PaperlessViewEducationDetailsResponseModel) {
                    showProgressBar.value = false
                    viewEducationDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    fun callGetEducationCategoryApi() {
        showProgressBar.value = true
        getEducationCategoryUseCase.invoke(
            viewModelScope,
            Any(),
            object : UseCaseResponse<GetEducationCategoryResponseModel> {
                override fun onSuccess(result: GetEducationCategoryResponseModel) {
                    showProgressBar.value = false
                    getEducationCategoryResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            })
    }

    fun callEducationalStreamApi(request: EducationalStreamRequestModel) {
        showProgressBar.value = true
        getEducationalStreamUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<EducationalStreamResponseModel> {
                override fun onSuccess(result: EducationalStreamResponseModel) {
                    showProgressBar.value = false
                    getEducationalStreamResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            }


        )
    }

    fun callPOBInsertEducationInfoApi(request: InsertEducationInfoRequestModel) {
        showProgressBar.value = true
        getPOBInsertEducationInfoUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertEducationInfoResponseModel> {
                override fun onSuccess(result: InsertEducationInfoResponseModel) {
                    showProgressBar.value = false
                    getPOBInsertEducationInfoResponseData.value = result
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