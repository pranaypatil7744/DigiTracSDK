package com.example.digitracksdk.domain.usecase.dashboard

import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.repository.home_repository.InnovIDCardRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class InnovIDCardUseCase constructor(private val innovIDCardRepository: InnovIDCardRepository) :
    UseCase<InnovIDCardResponseModel, Any?>() {

    override suspend fun run(params: Any?): InnovIDCardResponseModel {
        return innovIDCardRepository.callInnovIDCardApi(params as InnovIDCardRequestModel)
    }
}