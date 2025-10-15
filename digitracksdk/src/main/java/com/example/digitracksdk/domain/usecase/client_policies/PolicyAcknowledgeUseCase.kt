package com.example.digitracksdk.domain.usecase.client_policies

import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeResponseModel
import com.example.digitracksdk.domain.repository.client_policies_repository.ClientPoliciesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PolicyAcknowledgeUseCase(val repository: ClientPoliciesRepository) :
    UseCase<PolicyAcknowledgeResponseModel, Any>() {
    override suspend fun run(params: Any?): PolicyAcknowledgeResponseModel {
        return repository.callPolicyAcknowledgeApi(params as PolicyAcknowledgeRequestModel)
    }
}