package com.example.digitracksdk.domain.usecase.refer_friend_usecase

import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendResponseModel
import com.example.digitracksdk.domain.repository.refer_friend_repository.ReferFriendRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ReferralFriendUseCase constructor(
    private val referralFriendRepository: ReferFriendRepository
) : UseCase<ReferralFriendResponseModel, Any?>() {
    override suspend fun run(params: Any?): ReferralFriendResponseModel {
        return referralFriendRepository.callJobReferralApi(params as ReferralFriendRequestModel)
    }
}