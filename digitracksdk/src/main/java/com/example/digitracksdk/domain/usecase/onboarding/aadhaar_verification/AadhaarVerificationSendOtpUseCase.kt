package com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification

import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpResponseModel
import com.example.digitracksdk.domain.repository.onboarding.aadhar_verification.AadhaarVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AadhaarVerificationSendOtpUseCase constructor(private val aadhaarVerificationRepository: AadhaarVerificationRepository) :
    UseCase<AadhaarVerificationSendOtpResponseModel, Any?>() {
    override suspend fun run(params: Any?): AadhaarVerificationSendOtpResponseModel {
        return aadhaarVerificationRepository.callAadhaarVerificationSendOtpApi(params as AadhaarVerificationSendOtpRequestModel)
    }
}