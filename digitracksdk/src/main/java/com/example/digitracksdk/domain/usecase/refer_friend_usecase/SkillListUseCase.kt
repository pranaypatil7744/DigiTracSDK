package com.example.digitracksdk.domain.usecase.refer_friend_usecase

import com.example.digitracksdk.domain.model.refer_a_friend.SkillListResponseModel
import com.example.digitracksdk.domain.repository.refer_friend_repository.ReferFriendRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class SkillListUseCase constructor(private val referFriendRepository: ReferFriendRepository) :
    UseCase<SkillListResponseModel, Any?>() {
    override suspend fun run(params: Any?): SkillListResponseModel {
        return referFriendRepository.callSkillListApi()
    }
}