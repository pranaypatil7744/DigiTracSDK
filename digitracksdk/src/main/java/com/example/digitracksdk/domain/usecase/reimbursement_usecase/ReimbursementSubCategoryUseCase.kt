package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReimbursementSubCategoryUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ReimbursementSubCategoryResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReimbursementSubCategoryResponseModel {
        return reimbursementRepository.callReimbursementSubCategoryApi(params as ReimbursementSubCategoryRequestModel)
    }
}