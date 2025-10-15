package com.example.digitracksdk.domain.usecase.onboarding

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.onboarding.OnboardingDashboardResponseModel
import com.example.digitracksdk.domain.repository.onboarding.OnboardingDashboardRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class OnboardingDashboardUseCase constructor(private val onboardingDashboardRepository: OnboardingDashboardRepository):
    UseCase<OnboardingDashboardResponseModel, Any?>() {
    override suspend fun run(params: Any?): OnboardingDashboardResponseModel {
        return onboardingDashboardRepository.callOnboardingDashboardApi(params as CommonRequestModel)
    }
}