package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReimbursementModeOfTravelUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<ModeOfTravelResponseModel, Any?>() {
    override suspend fun run(params: Any?): ModeOfTravelResponseModel {
        return reimbursementRepository.callReimbursementModeOfTravelApi(params as ModeOfTravelRequestModel)
    }
}