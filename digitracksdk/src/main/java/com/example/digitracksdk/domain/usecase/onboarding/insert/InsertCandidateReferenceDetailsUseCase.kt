package com.example.digitracksdk.domain.usecase.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.insert.ReferenceCategoryRepository
import com.example.digitracksdk.domain.usecase.base.UseCase


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:54
 */
class InsertCandidateReferenceDetailsUseCase
constructor(private val repository: ReferenceCategoryRepository):
    UseCase<InsertCandidateReferenceDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): InsertCandidateReferenceDetailsResponseModel {
        return repository.callInsertCandidateReferenceDetailsApi(params as InsertCandidateReferenceDetailsModel)
    }
}