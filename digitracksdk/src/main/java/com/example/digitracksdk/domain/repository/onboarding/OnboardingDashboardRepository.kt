package com.example.digitracksdk.domain.repository.onboarding

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.OnboardingDashboardResponseModel

interface OnboardingDashboardRepository  {

    suspend fun callOnboardingDashboardApi(request: CommonRequestModel): OnboardingDashboardResponseModel

}