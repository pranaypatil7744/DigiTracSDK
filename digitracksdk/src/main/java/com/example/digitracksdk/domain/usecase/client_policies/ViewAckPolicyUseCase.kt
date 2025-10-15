package com.example.digitracksdk.domain.usecase.client_policies

import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyResponseModel
import com.example.digitracksdk.domain.repository.client_policies_repository.ClientPoliciesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ViewAckPolicyUseCase(val repository: ClientPoliciesRepository) : UseCase<ViewAckPolicyResponseModel, Any>() {
    override suspend fun run(params: Any?): ViewAckPolicyResponseModel {

        return repository.callViewAckPolicyApi(params as ViewAckPolicyRequestModel)
    }
}