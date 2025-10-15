package com.example.digitracksdk.data.repository.leaves_repo_imp

import com.example.digitracksdk.domain.model.leaves.ApplyLeaveRequestModel
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveResponseModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusRequestModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusResponseModel
import com.example.digitracksdk.domain.model.leaves.HolidaysListResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewResponseModel
import com.example.digitracksdk.domain.model.leaves.LeavesTypeResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveStatusSummaryResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository

class LeavesRepositoryImp(private val apiService: ApiService) : LeavesRepository {
    override suspend fun callHolidaysListApi(request: CommonRequestModel): HolidaysListResponseModel {
        return apiService.callHolidaysListApi(request)
    }

    override suspend fun callApplyLeavesApi(request: ApplyLeaveRequestModel): ApplyLeaveResponseModel {
        return apiService.callApplyLeaveApi(request)
    }

    override suspend fun callViewLeaveRequestApi(request: LeaveRequestViewRequestModel): LeaveRequestViewResponseModel {
        return apiService.callViewLeaveRequestApi(request)
    }

    override suspend fun callLeavesTypeApi(request: CommonRequestModel): LeavesTypeResponseModel {
        return apiService.callLeavesTypeApi(request)
    }

    override suspend fun callLeaveBalanceApi(request: LeaveBalanceRequestModel): LeaveBalanceResponseModel {
        return apiService.callLeaveBalanceApi(request)
    }

    override suspend fun callLeaveBalanceStatusApi(request: BalanceLeaveStatusRequestModel): BalanceLeaveStatusResponseModel {
        return apiService.callLeaveBalanceStatusApi(request)
    }

    override suspend fun callLeaveStatusSummaryApi(request: BalanceLeaveStatusRequestModel): LeaveStatusSummaryResponseModel {
        return apiService.callLeaveStatusSummaryApi(request)
    }

}