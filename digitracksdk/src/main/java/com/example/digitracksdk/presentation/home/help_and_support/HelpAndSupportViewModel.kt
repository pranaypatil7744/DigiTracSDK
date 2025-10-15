package com.example.digitracksdk.presentation.home.help_and_support

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryResponseModel
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueRequestModel
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueResponseModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListRequestModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryResponseModel
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.AssociateIssueUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.HelpAndSupportListUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.IssueCategoryUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.IssueDetailsUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.IssueSubCategoryUseCase
import kotlinx.coroutines.cancel

class HelpAndSupportViewModel(
    private val associateIssueUseCase: AssociateIssueUseCase,
    private val helpAndSupportListUseCase: HelpAndSupportListUseCase,
    private val issueCategoryUseCase: IssueCategoryUseCase,
    private val issueSubCategoryUseCase: IssueSubCategoryUseCase,
    private val issueDetailsUseCase: IssueDetailsUseCase
) : ViewModel() {
    var associateIssueResponseData = MutableLiveData<AssociateIssueResponseModel>()
    var issueCategoryResponseData = MutableLiveData<ArrayList<IssueCategoryResponseModel>>()
    var issueSubCategoryResponseData = MutableLiveData<ArrayList<IssueSubCategoryResponseModel>>()
    var helpAndSupportListResponseData = MutableLiveData<HelpAndSupportListResponseModel>()
    var issueDetailsResponseData = MutableLiveData<IssueDetailsResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val associateIssueMessageData = MutableLiveData<String>()
    val issueCategoryMessageData = MutableLiveData<String>()
    val issueSubCategoryMessageData = MutableLiveData<String>()
    val helpAndSupportMessageData = MutableLiveData<String>()

    fun callHelpAndSupportApi(request: HelpAndSupportListRequestModel) {
        showProgressbar.value = true
        helpAndSupportListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<HelpAndSupportListResponseModel> {
                override fun onSuccess(result: HelpAndSupportListResponseModel) {
                    helpAndSupportListResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    helpAndSupportMessageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }
            })
    }

    fun callIssueCategoryApi(request: IssueCategoryRequestModel) {
        showProgressbar.value = true
        issueCategoryUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ArrayList<IssueCategoryResponseModel>> {
                override fun onSuccess(result: ArrayList<IssueCategoryResponseModel>) {
                    issueCategoryResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    issueCategoryMessageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }
            })
    }

    fun callIssueSubCategoryApi(request: IssueSubCategoryRequestModel) {
        showProgressbar.value = true
        issueSubCategoryUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ArrayList<IssueSubCategoryResponseModel>> {
                override fun onSuccess(result: ArrayList<IssueSubCategoryResponseModel>) {
                    issueSubCategoryResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    issueSubCategoryMessageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }
            })
    }

    fun callAssociateIssueApi(request: AssociateIssueRequestModel) {
        showProgressbar.value = true
        associateIssueUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AssociateIssueResponseModel> {
                override fun onSuccess(result: AssociateIssueResponseModel) {
                    associateIssueResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    associateIssueMessageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }
            })
    }

    fun callIssueDetailsApi(request: IssueDetailsRequestModel) {
        showProgressbar.value = true
        issueDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<IssueDetailsResponseModel> {
                override fun onSuccess(result: IssueDetailsResponseModel) {
                    issueDetailsResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    associateIssueMessageData.value = apiError?.message.toString()
                    showProgressbar.value = false
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}