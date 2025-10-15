package com.example.digitracksdk.domain.usecase.onboarding.pan_verification

import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationResponseModel
import com.example.digitracksdk.domain.repository.onboarding.pan_verification.PanVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PanVerificationUseCase
constructor(private val panVerificationRepository: PanVerificationRepository) :
    UseCase<PanCardVerificationResponseModel, Any?>() {
    override suspend fun run(params: Any?): PanCardVerificationResponseModel {
        return panVerificationRepository.callPanVerificationApi(params as PanCardVerificationRequestModel)
    }
}