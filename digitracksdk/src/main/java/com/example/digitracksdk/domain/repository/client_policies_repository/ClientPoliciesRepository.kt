package com.example.digitracksdk.domain.repository.client_policies_repository

import com.example.digitracksdk.domain.model.client_policies.AcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeResponseModel
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesRequestModel
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesResponseModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeResponseModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyResponseModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyResponseModel

interface ClientPoliciesRepository {

    suspend fun callClientPolicies(request: ClientPoliciesRequestModel): ClientPoliciesResponseModel

    suspend fun callInsertAcknowledgeApi(request: AcknowledgeRequestModel): AcknowledgeResponseModel

    suspend fun callViewPolicyApi(request: ViewPolicyRequestModel): ViewPolicyResponseModel

    suspend fun callPolicyAcknowledgeApi(requestModel: PolicyAcknowledgeRequestModel) : PolicyAcknowledgeResponseModel

    suspend fun callViewAckPolicyApi(requestModel: ViewAckPolicyRequestModel) : ViewAckPolicyResponseModel
}