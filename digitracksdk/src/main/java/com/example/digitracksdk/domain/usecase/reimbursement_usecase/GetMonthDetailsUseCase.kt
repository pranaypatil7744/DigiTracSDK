package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthDetailsResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthYearDetailsRequestModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetMonthDetailsUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<GetMonthDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetMonthDetailsResponseModel {
        return reimbursementRepository.callGetMonthDetailApi(params as GetMonthYearDetailsRequestModel)
    }
}