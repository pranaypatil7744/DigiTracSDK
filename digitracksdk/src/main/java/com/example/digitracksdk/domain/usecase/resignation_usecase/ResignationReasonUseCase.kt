package com.example.digitracksdk.domain.usecase.resignation_usecase

import com.example.digitracksdk.domain.model.resignation.ResignationReasonRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonResponseModel
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ResignationReasonUseCase
    (val repository: ResignationRepository) : UseCase<ResignationReasonResponseModel, Any>()
{
    override suspend fun run(params: Any?): ResignationReasonResponseModel {
       return repository.callResignationReasonApi(params as ResignationReasonRequestModel)
    }
}