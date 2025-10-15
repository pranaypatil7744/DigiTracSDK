package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewFamilyDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.family_details.FamilyDetailsRepository


class FamilyDetailsRepositoryImp
    (private val apiService: ApiService) : FamilyDetailsRepository {
    override suspend fun callViewViewFamilyDetailsApi(request: InnovIDRequestModel): PaperlessViewFamilyDetailsResponseModel {
        return apiService.callGetFamilyDetailsApi(request)
    }

    override suspend fun callFamilyDetailsApi(request: PaperlessFamilyDetailsModel): PaperlessFamilyDetailsResponseModel {
        return apiService.callFamilyDetailsApi(request)
    }
}