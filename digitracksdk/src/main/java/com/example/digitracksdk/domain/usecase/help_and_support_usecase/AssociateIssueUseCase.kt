package com.example.digitracksdk.domain.usecase.help_and_support_usecase

import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueRequestModel
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueResponseModel
import com.example.digitracksdk.domain.repository.help_and_support_repository.HelpAndSupportRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AssociateIssueUseCase constructor(private val helpAndSupportRepository: HelpAndSupportRepository) :
    UseCase<AssociateIssueResponseModel, Any?>() {
    override suspend fun run(params: Any?): AssociateIssueResponseModel {
        return helpAndSupportRepository.callAssociateIssueApi(params as AssociateIssueRequestModel)
    }
}