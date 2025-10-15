package com.example.digitracksdk.presentation.attendance.attendance_regularization

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListResponseModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationTypeResponseModel
import com.innov.digitrac.domain.model.attendance_regularization_model.*
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.AttendanceRegularizationListUseCase
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.AttendanceRegularizationTypeUseCase
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.AttendanceRegularizationInsertUseCase
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.InsertAttendanceRegularizationUseCase
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils
import kotlinx.coroutines.cancel

class AttendanceRegularizationViewModel constructor(
    private val attendanceRegularizationListUseCase: AttendanceRegularizationListUseCase,
    private val attendanceRegularizationTypeUseCase: AttendanceRegularizationTypeUseCase,
    private val attendanceRegularizationInsertUseCase: AttendanceRegularizationInsertUseCase,
    private val insertAttendanceRegularizationUseCase: InsertAttendanceRegularizationUseCase
) : ViewModel() {

    val attendanceRegularizationListResponseData =
        MutableLiveData<AttendanceRegularizationListResponseModel>()
    val insertAttendanceRegularizationResponseData = MutableLiveData<InsertAttendanceRegularizationResponseModel>()
    val attendanceRegularizationTypeResponseData =
        MutableLiveData<AttendanceRegularizationTypeResponseModel>()
    val attendanceRegularizationInsertResponseData =
        MutableLiveData<InsertAttendanceRegularizationResponseModel>()
    val messageData = MutableLiveData<String>()

    var validationListener: ValidationListener? = null

    fun validateAttendanceRegularization(request: InsertAttendanceRegularizationRequestModel) {
        if (request.RequestTypeId == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.REQUEST_TYPE_ERROR,
                R.string.please_select_request_type
            )
            return
        }
        if (TextUtils.isEmpty(request.RegularizationDate)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FROM_DATE_ERROR,
                R.string.please_choose_date
            )
            return
        }
//        if (TextUtils.isEmpty(request.ToDate)) {
//            validationListener?.onValidationFailure(
//                Constant.ListenerConstants.TO_DATE_ERROR,
//                R.string.please_choose_to_date
//            )
//            return
//        }

        if (request.RequestTypeId == RequestType.OD.value || request.RequestTypeId == RequestType.ATTENDANCE.value){
            if (request.InTime == "00:00") {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.IN_TIME_ERROR,
                    R.string.please_choose_in_time
                )
                return
            }
            if (request.OutTime == "00:00") {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.OUT_TIME_ERROR,
                    R.string.please_choose_out_time
                )
                return
            }
            val inTime = request.InTime.replace(":",".").toFloat()
            val outTime = request.OutTime.replace(":",".").toFloat()
            if (inTime >= outTime){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.OUT_TIME_ERROR,
                    R.string.out_time_cannot_be_greater_than_in_time
                )
                return
            }
        }

        if (request.RequestTypeId == RequestType.OD.value){
            if (TextUtils.isEmpty(request.Location) || request.Location ==  "null") {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.LOCATION_ERROR,
                    R.string.please_enter_location
                )
                return
            }
            if (AppUtils.INSTANCE?.checkSpecialSymbol(request.Location) == true){
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.LOCATION_ERROR,
                    R.string.special_symbols_not_allowed_here
                )
                return
            }
        }

        if (TextUtils.isEmpty(request.Remarks)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.REMARK_ERROR,
                R.string.please_enter_remark
            )
            return
        }

        validationListener?.onValidationSuccess(Constant.success, R.string.success)

    }

    fun callAttendanceRegularizationTypeApi(request: CommonRequestModel) {
        attendanceRegularizationTypeUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceRegularizationTypeResponseModel> {
                override fun onSuccess(result: AttendanceRegularizationTypeResponseModel) {
                    attendanceRegularizationTypeResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callAttendanceRegularizationInsertApi(request: InsertAttendanceRegularizationRequestModel) {
        attendanceRegularizationInsertUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertAttendanceRegularizationResponseModel> {
                override fun onSuccess(result: InsertAttendanceRegularizationResponseModel) {
                    attendanceRegularizationInsertResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callInsertAttendanceRegularizationApi(request: InsertAttendanceRegularizationRequestModel) {
        insertAttendanceRegularizationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertAttendanceRegularizationResponseModel> {
                override fun onSuccess(result: InsertAttendanceRegularizationResponseModel) {
                    insertAttendanceRegularizationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callAttendanceRegularizationListApi(request: AttendanceRegularizationListRequestModel) {
        attendanceRegularizationListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AttendanceRegularizationListResponseModel> {
                override fun onSuccess(result: AttendanceRegularizationListResponseModel) {
                    attendanceRegularizationListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}