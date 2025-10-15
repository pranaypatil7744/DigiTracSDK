package com.example.digitracksdk.domain.usecase.resignation_usecase

import com.example.digitracksdk.domain.model.resignation.RevokeResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationResponseModel
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class RevokeResignationUseCase(val repository : ResignationRepository) : UseCase<RevokeResignationResponseModel, Any>()
{
    override suspend fun run(params: Any?): RevokeResignationResponseModel {
        return repository.callRevokeResignationApi(params as RevokeResignationRequestModel)
    }
}