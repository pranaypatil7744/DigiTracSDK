package com.example.digitracksdk.domain.usecase.client_policies

import com.example.digitracksdk.domain.model.client_policies.AcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeResponseModel
import com.example.digitracksdk.domain.repository.client_policies_repository.ClientPoliciesRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InsertAcknowledgeUseCase constructor(private val clientPoliciesRepository: ClientPoliciesRepository) :
    UseCase<AcknowledgeResponseModel, Any?>() {
    override suspend fun run(params: Any?): AcknowledgeResponseModel {
        return clientPoliciesRepository.callInsertAcknowledgeApi(params as AcknowledgeRequestModel)
    }
}