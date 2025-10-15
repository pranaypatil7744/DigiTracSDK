package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewCandidateDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewCandidateDetailsRepository

class PaperlessViewCandidateDetailsRepositoryImp
    (private val apiService: ApiService) : PaperlessViewCandidateDetailsRepository {
    override suspend fun callViewCandidateDetailsApi(request: InnovIDRequestModel): PaperlessViewCandidateDetailsResponseModel {
        return apiService.callGetCandidateDetailsApi(request)
    }

    override suspend fun callUpdateCandidateBasicDetailsApi(request: PaperlessUpdateCandidateBasicDetailsRequestModel): PaperlessUpdateCandidateBasicDetailsResponseModel {
        return apiService.callUpdateCandidateBasicDetailsApi(request)
    }
}