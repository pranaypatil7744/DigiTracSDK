package com.example.digitracksdk.domain.usecase.resignation_usecase

import com.example.digitracksdk.domain.model.resignation.ResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationResponseModel
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ResignationUseCase constructor(private val resignationRepository: ResignationRepository) :
    UseCase<ResignationResponseModel, Any?>() {
    override suspend fun run(params: Any?): ResignationResponseModel {
        return resignationRepository.callResignationApi(params as ResignationRequestModel)
    }
}