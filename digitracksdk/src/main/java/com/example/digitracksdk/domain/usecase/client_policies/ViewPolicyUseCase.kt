package com.example.digitracksdk.domain.usecase.client_policies

import com.example.digitracksdk.domain.model.client_policies.ViewPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyResponseModel
import com.example.digitracksdk.domain.repository.client_policies_repository.ClientPoliciesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewPolicyUseCase constructor(private val clientPoliciesRepository: ClientPoliciesRepository) :
    UseCase<ViewPolicyResponseModel, Any?>() {
    override suspend fun run(params: Any?): ViewPolicyResponseModel {
        return clientPoliciesRepository.callViewPolicyApi(params as ViewPolicyRequestModel)
    }
}