package com.example.digitracksdk.domain.usecase.help_and_support_usecase

import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListRequestModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListResponseModel
import com.example.digitracksdk.domain.repository.help_and_support_repository.HelpAndSupportRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class HelpAndSupportListUseCase constructor(private val helpAndSupportRepository: HelpAndSupportRepository) :
    UseCase<HelpAndSupportListResponseModel, Any?>() {
    override suspend fun run(params: Any?): HelpAndSupportListResponseModel {
        return helpAndSupportRepository.callHelpAndSupportList(params as HelpAndSupportListRequestModel)
    }
}