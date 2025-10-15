package com.example.digitracksdk.data.repository.help_and_support_repo_imp

import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListRequestModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueRequestModel
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsResponseModel
import com.example.digitracksdk.domain.repository.help_and_support_repository.HelpAndSupportRepository

class HelpAndSupportRepositoryImp(val apiService: ApiService) : HelpAndSupportRepository {
    override suspend fun callHelpAndSupportList(request: HelpAndSupportListRequestModel): HelpAndSupportListResponseModel {
        return apiService.callHelpAndSupportList(request)
    }

    override suspend fun callIssueCategoryApi(request: IssueCategoryRequestModel): ArrayList<IssueCategoryResponseModel> {
        return apiService.callIssueCategoryApi(request)
    }

    override suspend fun callIssueSubCategoryApi(request: IssueSubCategoryRequestModel): ArrayList<IssueSubCategoryResponseModel> {
        return apiService.callIssueSubCategoryApi(request)
    }

    override suspend fun callAssociateIssueApi(request: AssociateIssueRequestModel): AssociateIssueResponseModel {
        return apiService.callAssociateIssueApi(request)
    }

    override suspend fun callIssueDetailsApi(request: IssueDetailsRequestModel): IssueDetailsResponseModel {
        return apiService.callIssueDetailsApi(request)
    }
}