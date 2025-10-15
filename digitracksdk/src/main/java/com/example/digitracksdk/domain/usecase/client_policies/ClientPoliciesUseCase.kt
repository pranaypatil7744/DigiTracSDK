package com.example.digitracksdk.domain.usecase.client_policies

import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesRequestModel
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesResponseModel
import com.example.digitracksdk.domain.repository.client_policies_repository.ClientPoliciesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ClientPoliciesUseCase constructor
    (private val clientPoliciesRepository: ClientPoliciesRepository) :
    UseCase<ClientPoliciesResponseModel, Any?>() {
    override suspend fun run(params: Any?): ClientPoliciesResponseModel {
        return clientPoliciesRepository.callClientPolicies(params as ClientPoliciesRequestModel)
    }
}