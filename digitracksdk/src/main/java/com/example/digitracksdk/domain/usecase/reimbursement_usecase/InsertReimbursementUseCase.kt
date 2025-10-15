package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertReimbursementUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<InsertReimbursementResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertReimbursementResponseModel {
        return reimbursementRepository.callInsertReimbursementApi(params as InsertReimbursementRequestModel)
    }
}