package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.UpdatePendingReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdatePendingReimbursementResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UpdatePendingReimbursementUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<UpdatePendingReimbursementResponseModel, Any?>() {
    override suspend fun run(params: Any?): UpdatePendingReimbursementResponseModel {
        return reimbursementRepository.callUpdatePendingReimbursementApi(params as UpdatePendingReimbursementRequestModel)
    }
}