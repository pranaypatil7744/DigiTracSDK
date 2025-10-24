package com.example.digitracksdk.presentation.resignation.add_resignation

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.resignation.ResignationCategoryResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationListRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationListResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationResponseModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationResponseModel
import com.example.digitracksdk.domain.model.resignation.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationCategoryUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationListUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationNoticePeriodUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationReasonUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.RevokeResignationUseCase
import com.example.digitracksdk.listener.ValidationListener
import kotlinx.coroutines.cancel

class AddResignationViewModel(
    private val resignationCategoryUseCase: ResignationCategoryUseCase,
    private val resignationUseCase: ResignationUseCase,
    private val resignationListUseCase: ResignationListUseCase,
    private val resignationNoticePeriodUseCase: ResignationNoticePeriodUseCase,
    private val revokeResignationUseCase: RevokeResignationUseCase,
    private val resignationReasonUseCase: ResignationReasonUseCase

) :
    ViewModel() {

    val resignationCategoryResponseData = MutableLiveData<ResignationCategoryResponseModel>()
    val resignationResponseData = MutableLiveData<ResignationResponseModel>()
    val resignationListResponseData = MutableLiveData<ResignationListResponseModel>()
    val resignationNoticePeriodResponseData =
        MutableLiveData<ResignationNoticePeriodResponseModel>()
    val revokeResignationResponseData = MutableLiveData<RevokeResignationResponseModel>()
    val resignationReasonResponseData = MutableLiveData<ResignationReasonResponseModel>()
    var messageData = MutableLiveData<String>()
    val showProgressbar = MutableLiveData<Boolean>()

    var validationListener: ValidationListener? = null

    fun validateResignation(requestModel: ResignationRequestModel) {
        if (requestModel.ResignationCategoryId == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.RESIGNATION_CATEGORY_ID_ERROR,
                R.string.please_select_resignation_category
            )
            return
        }
        if (TextUtils.isEmpty(requestModel.ExpectedLastWorkingDate)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.LAST_WORKING_DATE_ERROR,
                R.string.please_select_preferred_last_working_date
            )
            return
        }
        if (TextUtils.isEmpty(requestModel.Reason)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.REASON_ERROR,
                R.string.please_enter_resignation_reason
            )
            return
        }
//        if (TextUtils.isEmpty(requestModel.ResgImageArr)) {
//            validationListener?.onValidationFailure(
//                Constant.ListenerConstants.IMAGE_ERROR,
//                R.string.please_upload_image
//            )
//            return
//        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }


    fun validateNewResignation(requestModel: ResignationRequestModel, resignationReasonId : Int) {
        if (requestModel.ResignationCategoryId == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.RESIGNATION_CATEGORY_ID_ERROR,
                R.string.please_select_resignation_category
            )
            return
        }
        if (TextUtils.isEmpty(requestModel.ExpectedLastWorkingDate)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.LAST_WORKING_DATE_ERROR,
                R.string.please_select_preferred_last_working_date
            )
            return
        }
        if (resignationReasonId == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.REASON_ERROR,
                R.string.reason_field_mandatory
            )
            return
        }
//        if (TextUtils.isEmpty(requestModel.ResgImageArr)) {
//            validationListener?.onValidationFailure(
//                Constant.ListenerConstants.IMAGE_ERROR,
//                R.string.please_upload_image
//            )
//            return
//        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }

    fun callResignationCategoryApi() {
        showProgressbar.value = true
        resignationCategoryUseCase.invoke(
            viewModelScope,
            null,
            object : UseCaseResponse<ResignationCategoryResponseModel> {
                override fun onSuccess(result: ResignationCategoryResponseModel) {
                    showProgressbar.value = false
                    resignationCategoryResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callResignationListApi(request: ResignationListRequestModel) {
        showProgressbar.value = true
        resignationListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ResignationListResponseModel> {
                override fun onSuccess(result: ResignationListResponseModel) {
                    showProgressbar.value = false
                    resignationListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callResignationApi(request: ResignationRequestModel) {
        showProgressbar.value = true
        resignationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ResignationResponseModel> {
                override fun onSuccess(result: ResignationResponseModel) {
                    showProgressbar.value = false
                    resignationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callResignationNoticePeriodApi(request: ResignationNoticePeriodRequestModel) {
        showProgressbar.value = true
        resignationNoticePeriodUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ResignationNoticePeriodResponseModel> {
                override fun onSuccess(result: ResignationNoticePeriodResponseModel) {
                    showProgressbar.value = false
                    resignationNoticePeriodResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            }

        )

    }

    fun callRevokeResignationApi(request: RevokeResignationRequestModel) {
        showProgressbar.value = true
        revokeResignationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<RevokeResignationResponseModel> {
                override fun onSuccess(result: RevokeResignationResponseModel) {
                    showProgressbar.value = false
                    revokeResignationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            }

        )

    }

    fun callResignationReasonApi(request: ResignationReasonRequestModel) {
        showProgressbar.value = true
        resignationReasonUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ResignationReasonResponseModel> {
                override fun onSuccess(result: ResignationReasonResponseModel) {

                    showProgressbar.value = false
                    resignationReasonResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }

            }

        )
    }


    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}