package com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification

import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationResponseModel
import com.example.digitracksdk.domain.repository.onboarding.aadhar_verification.AadhaarVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class AadhaarVerificationOtpValidationUseCase constructor(private val aadhaarVerificationRepository: AadhaarVerificationRepository) :
    UseCase<AadhaarVerificationOtpValidationResponseModel, Any?>() {
    override suspend fun run(params: Any?): AadhaarVerificationOtpValidationResponseModel {
        return aadhaarVerificationRepository.callAadhaarVerificationOtpValidationApi(params as AadhaarVerificationOtpValidationRequestModel)
    }
}