package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReimbursementCategoryUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ReimbursementCategoryResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReimbursementCategoryResponseModel {
        return reimbursementRepository.callReimbursementCategoryApi(params as ReimbursementCategoryRequestModel)
    }
}