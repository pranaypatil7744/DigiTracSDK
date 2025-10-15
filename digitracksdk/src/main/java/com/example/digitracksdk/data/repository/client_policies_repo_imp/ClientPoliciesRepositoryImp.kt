package com.example.digitracksdk.data.repository.client_policies_repo_imp

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
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.repository.client_policies_repository.ClientPoliciesRepository

class ClientPoliciesRepositoryImp(private val apiService: ApiService): ClientPoliciesRepository {
    override suspend fun callClientPolicies(request: ClientPoliciesRequestModel): ClientPoliciesResponseModel {
        return apiService.callClientPolicyApi(request)
    }

    override suspend fun callInsertAcknowledgeApi(request: AcknowledgeRequestModel): AcknowledgeResponseModel {
        return apiService.callInsertAcknowledgeApi(request)
    }

    override suspend fun callViewPolicyApi(request: ViewPolicyRequestModel): ViewPolicyResponseModel {
        return apiService.callViewPolicyApi(request)
    }

    override suspend fun callPolicyAcknowledgeApi(requestModel: PolicyAcknowledgeRequestModel): PolicyAcknowledgeResponseModel {

        return apiService.callPolicyAcknowledgeStatusApi(requestModel)
    }

    override suspend fun callViewAckPolicyApi(requestModel: ViewAckPolicyRequestModel): ViewAckPolicyResponseModel {

        return apiService.callViewAckPolicy(requestModel)
    }
}