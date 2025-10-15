package com.example.digitracksdk.domain.usecase.resignation_usecase

import com.example.digitracksdk.domain.model.resignation.ResignationListRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationListResponseModel
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ResignationListUseCase constructor(private val resignationRepository: ResignationRepository) :
    UseCase<ResignationListResponseModel, Any?>() {
    override suspend fun run(params: Any?): ResignationListResponseModel {
        return resignationRepository.callResignationListApi(params as ResignationListRequestModel)
    }
}