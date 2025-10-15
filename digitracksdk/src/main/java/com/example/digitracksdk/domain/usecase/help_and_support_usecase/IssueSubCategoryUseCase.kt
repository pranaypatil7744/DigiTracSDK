package com.example.digitracksdk.domain.usecase.help_and_support_usecase

import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryResponseModel
import com.example.digitracksdk.domain.repository.help_and_support_repository.HelpAndSupportRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class IssueSubCategoryUseCase constructor(private val helpAndSupportRepository: HelpAndSupportRepository) :
    UseCase<ArrayList<IssueSubCategoryResponseModel>, Any?>() {
    override suspend fun run(params: Any?): ArrayList<IssueSubCategoryResponseModel> {
        return helpAndSupportRepository.callIssueSubCategoryApi(params as IssueSubCategoryRequestModel)
    }
}