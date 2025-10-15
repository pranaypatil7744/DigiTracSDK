package com.example.digitracksdk.domain.usecase.reimbursement_usecase

import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthYearDetailsRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetYearDetailsResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetYearDetailsUseCase constructor(private val reimbursementRepository: ReimbursementRepository) :
    UseCase<GetYearDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetYearDetailsResponseModel {
        return reimbursementRepository.callGetYearDetailApi(params as GetMonthYearDetailsRequestModel)
    }
}