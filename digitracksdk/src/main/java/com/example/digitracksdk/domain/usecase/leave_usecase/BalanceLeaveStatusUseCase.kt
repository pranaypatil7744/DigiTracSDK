package com.example.digitracksdk.domain.usecase.leave_usecase

import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusRequestModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class BalanceLeaveStatusUseCase constructor(private val leavesRepository: LeavesRepository) :
    UseCase<BalanceLeaveStatusResponseModel, Any?>() {
    override suspend fun run(params: Any?): BalanceLeaveStatusResponseModel {
        return leavesRepository.callLeaveBalanceStatusApi(params as BalanceLeaveStatusRequestModel)
    }
}