package com.example.digitracksdk.presentation.attendance.mileage_tracking

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationResponseModel
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListResponseModel
import com.innov.digitrac.domain.model.mileage_tracking_model.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.InsertMileageRegularizationUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.InsertMileageTrackingUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.MileageRegularizationListUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.MileageTrackingFlagUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.MileageTrackingListUseCase
import com.innov.digitrac.domain.usecase.mileage_tracking_usecase.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.attendance.mileage_tracking.status_fragment.ReadingStatus
import kotlinx.coroutines.cancel

class MileageTrackingViewModel constructor(
    private val mileageRegularizationListUseCase: MileageRegularizationListUseCase,
    private val insertMileageRegularizationUseCase: InsertMileageRegularizationUseCase,
    private val insertMileageTrackingUseCase: InsertMileageTrackingUseCase,
    private val mileageTrackingFlagUseCase: MileageTrackingFlagUseCase,
    private val mileageTrackingListUseCase: MileageTrackingListUseCase
) : ViewModel() {

    val insertMileageRegularizationResponseData =
        MutableLiveData<InsertMileageRegularizationResponseModel>()
    val mileageRegularizationListResponseData =
        MutableLiveData<MileageRegularizationListResponseModel>()
    val mileageTrackingListResponseData = MutableLiveData<MileageTrackingListResponseModel>()
    val mileageTrackingFlagResponseData = MutableLiveData<MileageTrackingFlagResponseModel>()
    val insertMileageTrackingResponseData = MutableLiveData<InsertMileageTrackingResponseModel>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    fun callMileageTrackingFlagApi(request: MileageTrackingFlagRequestModel) {
        mileageTrackingFlagUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<MileageTrackingFlagResponseModel> {
                override fun onSuccess(result: MileageTrackingFlagResponseModel) {
                    mileageTrackingFlagResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callMileageTrackingListApi(request: MileageTrackingListRequestModel) {
        mileageTrackingListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<MileageTrackingListResponseModel> {
                override fun onSuccess(result: MileageTrackingListResponseModel) {
                    mileageTrackingListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callInsertMileageTrackingApi(request: InsertMileageTrackingRequestModel) {
        insertMileageTrackingUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertMileageTrackingResponseModel> {
                override fun onSuccess(result: InsertMileageTrackingResponseModel) {
                    insertMileageTrackingResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callInsertMileageRegularizationApi(request: InsertMileageRegularizationRequestModel) {
        insertMileageRegularizationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertMileageRegularizationResponseModel> {
                override fun onSuccess(result: InsertMileageRegularizationResponseModel) {
                    insertMileageRegularizationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callMileageRegularizationListApi(request: MileageRegularizationListRequestModel) {
        mileageRegularizationListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<MileageRegularizationListResponseModel> {
                override fun onSuccess(result: MileageRegularizationListResponseModel) {
                    mileageRegularizationListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun validateMileageRegularization(request: InsertMileageRegularizationRequestModel) {
        if (TextUtils.isEmpty(request.TravelDate)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FROM_DATE_ERROR,
                R.string.please_choose_regularization_date
            )
            return
        }

        if (TextUtils.isEmpty(request.StartReading)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.OPEN_READING_ERROR,
                R.string.please_enter_opening_reading
            )
            return
        }
        if (TextUtils.isEmpty(request.EndReading)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.CLOSE_READING_ERROR,
                R.string.please_enter_closing_reading
            )
            return
        }
        if ((request.StartReading?.toInt() ?: 0) >= (request.EndReading?.toInt() ?: 0)){
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.OPEN_READING_ERROR,
                R.string.opening_reading_cannot_be_greater_than_closing_reading
            )
            return
        }
        if (TextUtils.isEmpty(request.Remark)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.REMARK_ERROR,
                R.string.please_enter_reason
            )
            return
        }

        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }

    fun validateMileageTracking(request: InsertMileageTrackingRequestModel, readingStatus: Int) {
        if (TextUtils.isEmpty(request.TravelDate)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FROM_DATE_ERROR,
                R.string.please_choose_date
            )
            return
        }
        if (readingStatus == ReadingStatus.OPENING.Value) {

            if (TextUtils.isEmpty(request.StartReading)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.OPEN_READING_ERROR,
                    R.string.please_enter_opening_reading
                )
                return
            }
            if (TextUtils.isEmpty(request.StartReadingImageArr)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.IMAGE_ERROR,
                    R.string.please_upload_image
                )
                return
            }
        }
        if (readingStatus == ReadingStatus.CLOSING.Value) {

            if (TextUtils.isEmpty(request.EndReading)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.CLOSE_READING_ERROR,
                    R.string.please_enter_closing_reading
                )
                return
            }
            if (TextUtils.isEmpty(request.EndReadingImageArr)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.IMAGE_ERROR,
                    R.string.please_upload_image
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