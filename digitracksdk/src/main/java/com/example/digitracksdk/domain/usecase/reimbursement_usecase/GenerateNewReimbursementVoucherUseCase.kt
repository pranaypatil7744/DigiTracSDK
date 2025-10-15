package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GenerateNewReimbursementVoucherUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<GenerateVoucherFromNewReimbursementResponseModel, Any?>() {
    override suspend fun run(params: Any?): GenerateVoucherFromNewReimbursementResponseModel {
        return reimbursementRepository.callGenerateNewReimbursementVoucher(params as GenerateVoucherFromNewReimbursementRequestModel)
    }
}