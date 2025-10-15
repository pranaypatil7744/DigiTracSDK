package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementListRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementListResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class NewReimbursementPendingListUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ReimbursementListResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReimbursementListResponseModel {
        return reimbursementRepository.callGetNewReimbursementPendingListApi(params as ReimbursementListRequestModel)
    }

}