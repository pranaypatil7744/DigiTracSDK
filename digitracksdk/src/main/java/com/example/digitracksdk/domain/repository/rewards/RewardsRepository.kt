package com.example.digitracksdk.domain.repository.rewards

import com.example.digitracksdk.domain.model.rewards.RewardRequestModel
import com.example.digitracksdk.domain.model.rewards.RewardResponseModel

interface RewardsRepository {
    suspend fun callRewardsApi(request: RewardRequestModel): RewardResponseModel
}