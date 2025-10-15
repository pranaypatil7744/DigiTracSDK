package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1RequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1ResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CheckReimbursementLimitV1UseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<CheckReimbursementLimitV1ResponseModel, Any?>() {
    override suspend fun run(params: Any?): CheckReimbursementLimitV1ResponseModel {
        return reimbursementRepository.callCheckReimbursementLimitV1Api(params as CheckReimbursementLimitV1RequestModel)
    }
}