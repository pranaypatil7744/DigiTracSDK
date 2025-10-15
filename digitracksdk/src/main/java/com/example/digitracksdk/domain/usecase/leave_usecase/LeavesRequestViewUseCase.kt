package com.example.digitracksdk.domain.usecase.leave_usecase

import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class LeavesRequestViewUseCase constructor(private val leavesRepository: LeavesRepository) :
    UseCase<LeaveRequestViewResponseModel, Any?>() {
    override suspend fun run(params: Any?): LeaveRequestViewResponseModel {
        return leavesRepository.callViewLeaveRequestApi(params as LeaveRequestViewRequestModel)
    }
}