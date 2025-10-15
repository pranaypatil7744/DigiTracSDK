package com.example.digitracksdk.domain.usecase.refer_friend_usecase

import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsResponseModel
import com.example.digitracksdk.domain.repository.refer_friend_repository.ReferFriendRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class BranchDetailsListUseCase constructor(private val referFriendRepository: ReferFriendRepository) :
    UseCase<BranchDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): BranchDetailsResponseModel {
        return referFriendRepository.callBranchListApi(params as BranchDetailsRequestModel)
    }
}