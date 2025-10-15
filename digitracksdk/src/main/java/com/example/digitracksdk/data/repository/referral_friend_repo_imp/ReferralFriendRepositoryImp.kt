package com.example.digitracksdk.data.repository.referral_friend_repo_imp

import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.SkillListResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsResponseModel
import com.example.digitracksdk.domain.repository.refer_friend_repository.ReferFriendRepository

class ReferralFriendRepositoryImp (private val apiService: ApiService): ReferFriendRepository {
    override suspend fun callBranchListApi(request: BranchDetailsRequestModel): BranchDetailsResponseModel {
        return apiService.callBranchDetailsListApi(request)
    }

    override suspend fun callSkillListApi(): SkillListResponseModel {
       return apiService.callSkillListApi()
    }

    override suspend fun callJobReferralApi(request: ReferralFriendRequestModel): ReferralFriendResponseModel {
        return apiService.callJobReferralApi(request)
    }
}