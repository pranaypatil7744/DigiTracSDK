package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertNewReimbursementUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<InsertNewReimbursementResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertNewReimbursementResponseModel {
        return reimbursementRepository.callInsertNewReimbursementApi(params as InsertNewReimbursementRequestModel)
    }
}