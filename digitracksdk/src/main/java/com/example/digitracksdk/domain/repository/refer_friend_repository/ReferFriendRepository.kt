package com.example.digitracksdk.domain.repository.refer_friend_repository

import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.SkillListResponseModel

interface ReferFriendRepository {

    suspend fun callBranchListApi(request: BranchDetailsRequestModel): BranchDetailsResponseModel

    suspend fun callSkillListApi(): SkillListResponseModel

    suspend fun callJobReferralApi(request: ReferralFriendRequestModel): ReferralFriendResponseModel
}