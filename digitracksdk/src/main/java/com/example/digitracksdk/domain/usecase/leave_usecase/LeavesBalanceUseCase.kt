package com.example.digitracksdk.domain.usecase.leave_usecase

import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class LeavesBalanceUseCase constructor(private val leavesRepository: LeavesRepository) :
    UseCase<LeaveBalanceResponseModel, Any?>() {
    override suspend fun run(params: Any?): LeaveBalanceResponseModel {
        return leavesRepository.callLeaveBalanceApi(params as LeaveBalanceRequestModel)
    }
}