package com.example.digitracksdk.domain.usecase.resignation_usecase

import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodResponseModel
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ResignationNoticePeriodUseCase(val repository : ResignationRepository) : UseCase<ResignationNoticePeriodResponseModel, Any>() {
    override suspend fun run(params: Any?): ResignationNoticePeriodResponseModel {
       return repository.callResignationNoticePeriodApi(params as ResignationNoticePeriodRequestModel)
    }
}