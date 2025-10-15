package com.example.digitracksdk.domain.repository.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.GetReferenceCategoryModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsResponseModel


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,15:44
 */
interface ReferenceCategoryRepository {

    suspend fun callReferenceCategoryApi(): GetReferenceCategoryModel
    suspend fun callInsertCandidateReferenceDetailsApi(request: InsertCandidateReferenceDetailsModel): InsertCandidateReferenceDetailsResponseModel
}

