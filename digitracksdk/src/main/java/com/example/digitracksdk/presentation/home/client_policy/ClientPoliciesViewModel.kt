package com.example.digitracksdk.presentation.home.client_policy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesRequestModel
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesResponseModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyResponseModel
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeResponseModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeResponseModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.client_policies.ClientPoliciesUseCase
import com.example.digitracksdk.domain.usecase.client_policies.InsertAcknowledgeUseCase
import com.example.digitracksdk.domain.usecase.client_policies.PolicyAcknowledgeUseCase
import com.example.digitracksdk.domain.usecase.client_policies.ViewAckPolicyUseCase
import com.example.digitracksdk.domain.usecase.client_policies.ViewPolicyUseCase
import kotlinx.coroutines.cancel

class ClientPoliciesViewModel(
    private val clientPoliciesUseCase: ClientPoliciesUseCase,
    private val acknowledgeUseCase: InsertAcknowledgeUseCase,
    private val viewPolicyUseCase: ViewPolicyUseCase,
    private val policyAcknowledgeUseCase: PolicyAcknowledgeUseCase,
    private val viewAckPolicyUseCase: ViewAckPolicyUseCase
) :
    ViewModel() {

    val clientPoliciesResponseData = MutableLiveData<ClientPoliciesResponseModel>()
    val insertAcknowledgeResponseData = MutableLiveData<AcknowledgeResponseModel>()
    val viewPolicyResponseData = MutableLiveData<ViewPolicyResponseModel>()
    val policyAcknowledgeResponseData = MutableLiveData<PolicyAcknowledgeResponseModel>()
    val viewAckPolicyResponseData = MutableLiveData<ViewAckPolicyResponseModel>()
    val showProgressBar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callViewPolicyApi(request: ViewPolicyRequestModel) {
        showProgressBar.value = true
        viewPolicyUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ViewPolicyResponseModel> {
                override fun onSuccess(result: ViewPolicyResponseModel) {
                    showProgressBar.value = false
                    viewPolicyResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callClientPoliciesApi(request: ClientPoliciesRequestModel) {
        showProgressBar.value = true
        clientPoliciesUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ClientPoliciesResponseModel> {
                override fun onSuccess(result: ClientPoliciesResponseModel) {
                    showProgressBar.value = false
                    clientPoliciesResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callInsertAcknowledgeApi(request: AcknowledgeRequestModel) {
        showProgressBar.value = true
        acknowledgeUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AcknowledgeResponseModel> {
                override fun onSuccess(result: AcknowledgeResponseModel) {
                    showProgressBar.value = false
                    insertAcknowledgeResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressBar.value = false
                    messageData.value = apiError?.message.toString()
                }

            })
    }

    fun callPolicyAcknowledgeApi(request: PolicyAcknowledgeRequestModel) {
        showProgressBar.value = true
        policyAcknowledgeUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PolicyAcknowledgeResponseModel> {
                override fun onSuccess(result: PolicyAcknowledgeResponseModel) {
                    policyAcknowledgeResponseData.value = result
                    showProgressBar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                    showProgressBar.value = false
                }

            }
        )

    }

    fun callViewAckPolicyApi(requestModel: ViewAckPolicyRequestModel) {
        showProgressBar.value = true
        viewAckPolicyUseCase.invoke(
            viewModelScope,
            requestModel,
            object : UseCaseResponse<ViewAckPolicyResponseModel> {
                override fun onSuccess(result: ViewAckPolicyResponseModel) {
                    viewAckPolicyResponseData.value = result
                    showProgressBar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressBar.value = false
                }


            }
        )
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}