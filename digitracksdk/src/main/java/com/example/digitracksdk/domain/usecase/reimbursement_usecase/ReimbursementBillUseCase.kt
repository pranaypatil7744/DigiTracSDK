package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementBillResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReimbursementBillUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ReimbursementBillResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReimbursementBillResponseModel {
        return reimbursementRepository.callReimbursementBillApi(params as ReimbursementDetailsRequestModel)
    }
}