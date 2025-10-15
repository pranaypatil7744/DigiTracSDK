package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class DeleteNewReimbursementListItemUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<DeleteNewReimbursementItemResponseModel, Any?>() {
    override suspend fun run(params: Any?): DeleteNewReimbursementItemResponseModel {
        return reimbursementRepository.callDeleteNewReimbursementListItem(params as DeleteNewReimbursementItemRequestModel)
    }
}