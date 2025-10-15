package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UpdateReimbursementStatusUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<UpdateReimbursementStatusResponseModel, Any?>() {
    override suspend fun run(params: Any?): UpdateReimbursementStatusResponseModel {
        return reimbursementRepository.callUpdateReimbursementStatusApi(params as UpdateReimbursementStatusRequestModel)
    }
}