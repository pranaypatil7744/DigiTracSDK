package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.RejectedReimbursementValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedReimbursementValidationResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class RejectedReimbursementValidationUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<RejectedReimbursementValidationResponseModel, Any?>() {
    override suspend fun run(params: Any?): RejectedReimbursementValidationResponseModel {
        return reimbursementRepository.callRejectedReimbursementValidationApi(params as RejectedReimbursementValidationRequestModel)
    }
}