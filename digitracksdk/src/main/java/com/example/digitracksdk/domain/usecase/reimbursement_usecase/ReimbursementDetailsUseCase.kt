package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReimbursementDetailsUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ReimbursementDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReimbursementDetailsResponseModel {
        return reimbursementRepository.callReimbursementDetailsApi(params as ReimbursementDetailsRequestModel)
    }
}