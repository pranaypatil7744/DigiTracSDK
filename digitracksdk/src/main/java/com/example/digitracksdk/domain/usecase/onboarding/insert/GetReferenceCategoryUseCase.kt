package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.GetReferenceCategoryModel
import com.example.digitracksdk.domain.repository.onboarding.insert.ReferenceCategoryRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class GetReferenceCategoryUseCase
constructor(private val repository: ReferenceCategoryRepository):
    UseCase<GetReferenceCategoryModel, Any?>()
{
    override suspend fun run(params: Any?): GetReferenceCategoryModel {
        return repository.callReferenceCategoryApi()
    }
}