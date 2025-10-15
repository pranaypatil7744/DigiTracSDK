package com.example.digitracksdk.domain.usecase.resignation_usecase

import com.example.digitracksdk.domain.model.resignation.ResignationCategoryResponseModel
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ResignationCategoryUseCase constructor(private val resignationRepository: ResignationRepository) :
    UseCase<ResignationCategoryResponseModel, Any>() {
    override suspend fun run(params: Any?): ResignationCategoryResponseModel {
        return resignationRepository.callResignationCategoryApi()
    }
}