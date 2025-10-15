package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReimbursementEndKmUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ReimbursementEndKmResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReimbursementEndKmResponseModel {
        return reimbursementRepository.callReimbursementEndKmApi(params as ReimbursementEndKmRequestModel)
    }
}