package com.example.digitracksdk.domain.usecase.help_and_support_usecase

import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsResponseModel
import com.example.digitracksdk.domain.repository.help_and_support_repository.HelpAndSupportRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class IssueDetailsUseCase constructor(private val helpAndSupportRepository: HelpAndSupportRepository) :
    UseCase<IssueDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): IssueDetailsResponseModel {
        return helpAndSupportRepository.callIssueDetailsApi(params as IssueDetailsRequestModel)
    }
}