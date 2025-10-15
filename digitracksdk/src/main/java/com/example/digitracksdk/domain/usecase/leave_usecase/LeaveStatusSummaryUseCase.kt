package com.example.digitracksdk.domain.usecase.leave_usecase

import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveStatusSummaryResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class LeaveStatusSummaryUseCase constructor(private val leavesRepository: LeavesRepository) :
    UseCase<LeaveStatusSummaryResponseModel, Any?>() {
    override suspend fun run(params: Any?): LeaveStatusSummaryResponseModel {
        return leavesRepository.callLeaveStatusSummaryApi(params as BalanceLeaveStatusRequestModel)
    }
}