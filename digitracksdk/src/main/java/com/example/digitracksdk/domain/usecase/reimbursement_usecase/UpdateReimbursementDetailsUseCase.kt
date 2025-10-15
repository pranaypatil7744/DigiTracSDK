package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UpdateReimbursementDetailsUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<UpdateReimbursementDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): UpdateReimbursementDetailsResponseModel {
        return reimbursementRepository.callUpdateReimbursementDetailsApi(params as UpdateReimbursementDetailsRequestModel)
    }
}