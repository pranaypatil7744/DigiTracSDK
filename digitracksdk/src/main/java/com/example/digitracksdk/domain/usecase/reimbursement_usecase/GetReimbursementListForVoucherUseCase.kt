package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetNewReimbursementListResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetReimbursementListForVoucherUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<GetNewReimbursementListResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetNewReimbursementListResponseModel {
        return reimbursementRepository.callGetReimbursementListForVoucherApi(params as GnetIdRequestModel)
    }
}