package com.example.digitracksdk.presentation.leaves

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveRequestModel
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewResponseModel
import com.example.digitracksdk.domain.model.leaves.LeavesTypeResponseModel
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusRequestModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusResponseModel
import com.example.digitracksdk.domain.model.leaves.HolidaysListResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveStatusSummaryResponseModel
import com.innov.digitrac.domain.model.leaves.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.leave_usecase.ApplyLeavesUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.BalanceLeaveStatusUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.HolidaysListUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeaveStatusSummaryUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeavesBalanceUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeavesRequestViewUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeavesTypeUseCase
import com.innov.digitrac.domain.usecase.leave_usecase.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.presentation.leaves.apply_leave.LeaveTypes
import kotlinx.coroutines.cancel

class LeavesViewModel constructor(
    private val holidaysListUseCase: HolidaysListUseCase,
    private val applyLeavesUseCase: ApplyLeavesUseCase,
    private val leavesBalanceUseCase: LeavesBalanceUseCase,
    private val leavesRequestViewUseCase: LeavesRequestViewUseCase,
    private val leavesTypeUseCase: LeavesTypeUseCase,
    private val balanceLeaveStatusUseCase: BalanceLeaveStatusUseCase,
    private val leaveStatusSummaryUseCase: LeaveStatusSummaryUseCase
) :
    ViewModel() {

    val holidaysListResponse = MutableLiveData<HolidaysListResponseModel>()
    val balanceLeaveStatusResponse = MutableLiveData<BalanceLeaveStatusResponseModel>()
    val leaveStatusSummaryResponse = MutableLiveData<LeaveStatusSummaryResponseModel>()
    val applyLeavesResponse = MutableLiveData<ApplyLeaveResponseModel>()
    val leavesBalanceResponse = MutableLiveData<LeaveBalanceResponseModel>()
    val viewLeavesRequestListResponse = MutableLiveData<LeaveRequestViewResponseModel>()
    val leavesTypeResponse = MutableLiveData<LeavesTypeResponseModel>()
    val messageData = MutableLiveData<String>()
    val showProgressBar = MutableLiveData<Boolean>()

    var validationListener: ValidationListener? = null

    fun validateApplyLeaveRequest(request: ApplyLeaveRequestModel, context:Context) {
        if (request.RequestTypeId == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.LEAVE_TYPE_ERROR,
                R.string.please_select_leave_type
            )
            return
        }
        if (request.LeaveAppliedFor == context.getString(R.string.Select)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.LEAVE_SUB_TYPE_ERROR,
                R.string.please_select_leave_sub_type
            )
            return
        }
        if (TextUtils.isEmpty(request.RegularizationDate)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.FROM_DATE_ERROR,
                R.string.please_choose_from_date
            )
            return
        }
        if ((request.RequestTypeId != LeaveTypes.BirthdayLeave.value) &&
            (request.RequestTypeId != LeaveTypes.OptionalLeave.value) && request.LeaveAppliedFor != context.getString(R.string.half_day)){
            if (TextUtils.isEmpty(request.ToDate)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.TO_DATE_ERROR,
                    R.string.please_choose_to_date
                )
                return
            }
        }

        if (TextUtils.isEmpty(request.Remarks)) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.REASON_ERROR,
                R.string.please_enter_reason
            )
            return
        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)
    }

    fun callHolidaysListApi(request: CommonRequestModel) {
        showProgressBar.value = true
        holidaysListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<HolidaysListResponseModel> {
                override fun onSuccess(result: HolidaysListResponseModel) {
                    showProgressBar.value = false
                    holidaysListResponse.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callBalanceLeaveStatusApi(request: BalanceLeaveStatusRequestModel) {
        showProgressBar.value = true
        balanceLeaveStatusUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<BalanceLeaveStatusResponseModel> {
                override fun onSuccess(result: BalanceLeaveStatusResponseModel) {
                    showProgressBar.value = false
                    balanceLeaveStatusResponse.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callLeaveStatusSummaryApi(request: BalanceLeaveStatusRequestModel) {
        showProgressBar.value = true
        leaveStatusSummaryUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<LeaveStatusSummaryResponseModel> {
                override fun onSuccess(result: LeaveStatusSummaryResponseModel) {
                    showProgressBar.value = false
                    leaveStatusSummaryResponse.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callApplyLeaveApi(request: ApplyLeaveRequestModel) {
        showProgressBar.value = true
        applyLeavesUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ApplyLeaveResponseModel> {
                override fun onSuccess(result: ApplyLeaveResponseModel) {
                    showProgressBar.value = false
                    applyLeavesResponse.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callLeaveBalanceApi(request: LeaveBalanceRequestModel) {
        showProgressBar.value = true
        leavesBalanceUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<LeaveBalanceResponseModel> {
                override fun onSuccess(result: LeaveBalanceResponseModel) {
                    showProgressBar.value = false
                    leavesBalanceResponse.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callViewLeaveRequestListApi(request: LeaveRequestViewRequestModel) {
        showProgressBar.value = true
        leavesRequestViewUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<LeaveRequestViewResponseModel> {
                override fun onSuccess(result: LeaveRequestViewResponseModel) {
                    showProgressBar.value = false
                    viewLeavesRequestListResponse.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callLeavesTypeApi(request: CommonRequestModel) {
        showProgressBar.value = true
        leavesTypeUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<LeavesTypeResponseModel> {
                override fun onSuccess(result: LeavesTypeResponseModel) {
                    showProgressBar.value = false
                    leavesTypeResponse.value = result
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