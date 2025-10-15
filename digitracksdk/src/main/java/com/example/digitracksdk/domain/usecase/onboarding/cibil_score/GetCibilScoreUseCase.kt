package com.example.digitracksdk.domain.usecase.onboarding.cibil_score

import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreRequestModel
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreResponseModel
import com.example.digitracksdk.domain.repository.onboarding.cibil_score.CibilScoreRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetCibilScoreUseCase constructor(private val cibilScoreRepository: CibilScoreRepository):
UseCase<GetCibilScoreResponseModel, Any?>(){
    override suspend fun run(params: Any?): GetCibilScoreResponseModel {
        return cibilScoreRepository.callCibilScoreApi(params as GetCibilScoreRequestModel)
    }
}