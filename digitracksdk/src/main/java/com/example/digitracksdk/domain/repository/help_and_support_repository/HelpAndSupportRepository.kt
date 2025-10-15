package com.example.digitracksdk.domain.repository.help_and_support_repository

import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueRequestModel
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueResponseModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListRequestModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryResponseModel

interface HelpAndSupportRepository {

    suspend fun callHelpAndSupportList(request: HelpAndSupportListRequestModel): HelpAndSupportListResponseModel

    suspend fun callIssueCategoryApi(request: IssueCategoryRequestModel):ArrayList<IssueCategoryResponseModel>

    suspend fun callIssueSubCategoryApi(request: IssueSubCategoryRequestModel):ArrayList<IssueSubCategoryResponseModel>

    suspend fun callAssociateIssueApi(request: AssociateIssueRequestModel): AssociateIssueResponseModel

    suspend fun callIssueDetailsApi(request: IssueDetailsRequestModel): IssueDetailsResponseModel
}