package com.example.digitracksdk.domain.usecase.rewards_usecase

import com.example.digitracksdk.domain.model.rewards.RewardRequestModel
import com.example.digitracksdk.domain.model.rewards.RewardResponseModel
import com.example.digitracksdk.domain.repository.rewards.RewardsRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class RewardsUseCase (private  val  repository: RewardsRepository)  :  UseCase<RewardResponseModel, Any?>(){
    override suspend fun run(params: Any?): RewardResponseModel {
        return repository.callRewardsApi(params as RewardRequestModel)
    }
}