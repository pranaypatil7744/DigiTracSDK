package com.example.digitracksdk.domain.usecase.refine

import com.example.digitracksdk.domain.model.refine.RefineRequest
import com.example.digitracksdk.domain.model.refine.RefineResponseModel
import com.example.digitracksdk.domain.repository.refine.RefineRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class RefineUseCase constructor(private val refineRepository: RefineRepository) :
    UseCase<RefineResponseModel, Any?>() {
    override suspend fun run(params: Any?): RefineResponseModel {
        return refineRepository.callRefineUrlApi(params as RefineRequest)
    }
}