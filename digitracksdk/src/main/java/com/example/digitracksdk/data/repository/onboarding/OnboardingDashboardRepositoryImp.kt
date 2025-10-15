package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.OnboardingDashboardResponseModel
import com.example.digitracksdk.domain.repository.onboarding.OnboardingDashboardRepository

class OnboardingDashboardRepositoryImp constructor(private val apiService: ApiService):
    OnboardingDashboardRepository {
    override suspend fun callOnboardingDashboardApi(request: CommonRequestModel): OnboardingDashboardResponseModel {
        return apiService.callOnboardingDashboardApi(request)
    }
}