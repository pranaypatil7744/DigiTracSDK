package com.example.digitracksdk.data.repository.onboarding.insert

import com.example.digitracksdk.domain.model.onboarding.insert.GetReferenceCategoryModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.repository.onboarding.insert.ReferenceCategoryRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class ReferenceCategoryRepositoryImp
    (private val apiService: ApiService) : ReferenceCategoryRepository {

    override suspend fun callReferenceCategoryApi(): GetReferenceCategoryModel {
        return apiService.callGetReferenceCategoryApi()
    }

    override suspend fun callInsertCandidateReferenceDetailsApi(request: InsertCandidateReferenceDetailsModel): InsertCandidateReferenceDetailsResponseModel {
       return apiService.callInsertCandidateReferenceDetailsApi(request)
    }
}