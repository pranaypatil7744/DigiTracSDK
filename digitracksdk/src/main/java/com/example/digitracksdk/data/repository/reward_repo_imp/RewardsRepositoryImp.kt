package com.example.digitracksdk.data.repository.reward_repo_imp

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.rewards.RewardRequestModel
import com.example.digitracksdk.domain.model.rewards.RewardResponseModel
import com.example.digitracksdk.domain.repository.rewards.RewardsRepository

class RewardsRepositoryImp(private val repository: ApiService) : RewardsRepository {
    override suspend fun callRewardsApi(request: RewardRequestModel): RewardResponseModel {


        return repository.callRewardsApi(request)
    }
}