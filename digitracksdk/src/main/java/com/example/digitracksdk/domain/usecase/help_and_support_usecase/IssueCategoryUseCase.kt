package com.example.digitracksdk.domain.usecase.help_and_support_usecase

import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryResponseModel
import com.example.digitracksdk.domain.repository.help_and_support_repository.HelpAndSupportRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class IssueCategoryUseCase constructor(private val helpAndSupportRepository: HelpAndSupportRepository) :
    UseCase<ArrayList<IssueCategoryResponseModel>, Any?>() {
    override suspend fun run(params: Any?): ArrayList<IssueCategoryResponseModel> {
        return helpAndSupportRepository.callIssueCategoryApi(params as IssueCategoryRequestModel)
    }
}