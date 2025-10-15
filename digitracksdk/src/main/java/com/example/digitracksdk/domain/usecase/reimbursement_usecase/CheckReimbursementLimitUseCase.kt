package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CheckReimbursementLimitUseCase constructor(private val reimbursementRepository: ReimbursementRepository):
    UseCase<CheckReimbursementLimitResponseModel, Any?>() {
    override suspend fun run(params: Any?): CheckReimbursementLimitResponseModel {
        return reimbursementRepository.callCheckReimbursementLimitApi(params as CheckReimbursementLimitRequestModel)
    }
}