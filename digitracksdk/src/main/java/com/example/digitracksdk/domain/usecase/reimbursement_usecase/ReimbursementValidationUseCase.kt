package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementValidationResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReimbursementValidationUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ReimbursementValidationResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReimbursementValidationResponseModel {
        return reimbursementRepository.callReimbursementValidationApi(params as ReimbursementValidationRequestModel)
    }
}