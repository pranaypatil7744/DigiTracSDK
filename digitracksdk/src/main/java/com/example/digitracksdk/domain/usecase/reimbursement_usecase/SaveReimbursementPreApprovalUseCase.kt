package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.SaveReimbursementPreApprovalRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.SaveReimbursementPreApprovalResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class SaveReimbursementPreApprovalUseCase
constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<SaveReimbursementPreApprovalResponseModel, Any?>() {
    override suspend fun run(params: Any?): SaveReimbursementPreApprovalResponseModel {
        return reimbursementRepository.callSaveReimbursementPreApprovalApi(params as SaveReimbursementPreApprovalRequestModel)
    }
}