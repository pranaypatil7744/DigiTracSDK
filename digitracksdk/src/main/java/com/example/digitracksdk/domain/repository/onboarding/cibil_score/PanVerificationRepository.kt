package com.example.digitracksdk.domain.repository.onboarding.cibil_score

import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreRequestModel
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreResponseModel

interface CibilScoreRepository{

    suspend fun callCibilScoreApi(request: GetCibilScoreRequestModel): GetCibilScoreResponseModel

}