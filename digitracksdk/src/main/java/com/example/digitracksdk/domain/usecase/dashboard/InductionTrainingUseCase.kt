package com.example.digitracksdk.domain.usecase.dashboard

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.home_model.InductionTrainingResponseModel
import com.example.digitracksdk.domain.repository.home_repository.HomeRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InductionTrainingUseCase
    (private val homeRepository: HomeRepository) : UseCase<InductionTrainingResponseModel, Any>()
{
    override suspend fun run(params: Any?): InductionTrainingResponseModel {
        return homeRepository.callInductionTrainingApi(params as CommonRequestModel)
    }
}