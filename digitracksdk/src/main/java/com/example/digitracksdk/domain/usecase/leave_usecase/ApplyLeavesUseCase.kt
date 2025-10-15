package com.example.digitracksdk.domain.usecase.leave_usecase

import com.example.digitracksdk.domain.model.leaves.ApplyLeaveRequestModel
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ApplyLeavesUseCase constructor(private val leavesRepository: LeavesRepository) :
    UseCase<ApplyLeaveResponseModel, Any?>() {
    override suspend fun run(params: Any?): ApplyLeaveResponseModel {
        return leavesRepository.callApplyLeavesApi(params as ApplyLeaveRequestModel)
    }
}