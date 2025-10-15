package com.example.digitracksdk.domain.usecase.onboarding.pan_verification

import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsResponseModel
import com.example.digitracksdk.domain.repository.onboarding.pan_verification.PanVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 05-10-2023.
 */
class PanVerificationDetailsUseCase constructor(
    private val repository: PanVerificationRepository
) : UseCase<GetPanCardVerificationDetailsResponseModel, Any?>()
{
    override suspend fun run(params: Any?): GetPanCardVerificationDetailsResponseModel {
        return repository.callPanVerificationDetailsApi(params as GetPanCardVerificationDetailsRequestModel)
    }
}