package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewCandidateReferenceDetailsRepository


/**
 * Created by Mo. Khurseed Ansari on 01-September-2021,12:59
 */
class PaperlessViewCandidateReferenceDetailsRepositoryImp
    (private val apiService: ApiService) : PaperlessViewCandidateReferenceDetailsRepository {
    override suspend fun callViewCandidateReferenceDetailsApi(request: InnovIDRequestModel): PaperlessViewGetCandidateReferenceDetailsResponseModel {
        return apiService.callGetCandidateReferenceDetailsApi(request)
    }
}