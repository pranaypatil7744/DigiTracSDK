package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.RejectedEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedEndKmResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class RejectedEndKmUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<RejectedEndKmResponseModel, Any?>() {
    override suspend fun run(params: Any?): RejectedEndKmResponseModel {
        return reimbursementRepository.callRejectedEndKmApi(params as RejectedEndKmRequestModel)
    }
}