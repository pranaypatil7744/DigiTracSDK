package com.example.digitracksdk.domain.usecase.leave_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.leaves.LeavesTypeResponseModel
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class LeavesTypeUseCase constructor(private val leavesRepository: LeavesRepository) :
    UseCase<LeavesTypeResponseModel, Any?>() {
    override suspend fun run(params: Any?): LeavesTypeResponseModel {
        return leavesRepository.callLeavesTypeApi(params as CommonRequestModel)
    }
}