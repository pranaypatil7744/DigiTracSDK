package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.UploadReimbursementBillRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UploadReimbursementBillResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class UploadReimbursementBillUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<UploadReimbursementBillResponseModel, Any?>() {
    override suspend fun run(params: Any?): UploadReimbursementBillResponseModel {
        return reimbursementRepository.callUploadReimbursementBillApi(params as UploadReimbursementBillRequestModel)
    }
}