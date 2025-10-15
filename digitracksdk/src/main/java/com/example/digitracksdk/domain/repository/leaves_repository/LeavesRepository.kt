package com.example.digitracksdk.domain.repository.leaves_repository

import com.example.digitracksdk.domain.model.leaves.ApplyLeaveRequestModel
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveResponseModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusRequestModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusResponseModel
import com.example.digitracksdk.domain.model.leaves.HolidaysListResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewResponseModel
import com.example.digitracksdk.domain.model.leaves.LeavesTypeResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveStatusSummaryResponseModel

interface LeavesRepository {

    suspend fun callHolidaysListApi(request: CommonRequestModel): HolidaysListResponseModel
    suspend fun callApplyLeavesApi(request: ApplyLeaveRequestModel): ApplyLeaveResponseModel
    suspend fun callViewLeaveRequestApi(request: LeaveRequestViewRequestModel): LeaveRequestViewResponseModel
    suspend fun callLeavesTypeApi(request: CommonRequestModel): LeavesTypeResponseModel
    suspend fun callLeaveBalanceApi(request: LeaveBalanceRequestModel): LeaveBalanceResponseModel
    suspend fun callLeaveBalanceStatusApi(request: BalanceLeaveStatusRequestModel): BalanceLeaveStatusResponseModel
    suspend fun callLeaveStatusSummaryApi(request: BalanceLeaveStatusRequestModel): LeaveStatusSummaryResponseModel

}