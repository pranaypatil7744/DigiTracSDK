package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.PendingReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.PendingReimbursementResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PendingReimbursementUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<PendingReimbursementResponseModel, Any?>() {
    override suspend fun run(params: Any?): PendingReimbursementResponseModel {
        return reimbursementRepository.callPendingReimbursementListApi(params as PendingReimbursementRequestModel)
    }
}