package com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification

import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarResponseModel
import com.example.digitracksdk.domain.repository.onboarding.aadhar_verification.AadhaarVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class ValidateAadhaarUseCase constructor(private val aadhaarVerificationRepository: AadhaarVerificationRepository) :
    UseCase<ValidateAadhaarResponseModel, Any?>() {
    override suspend fun run(params: Any?): ValidateAadhaarResponseModel {
        return aadhaarVerificationRepository.callValidateAadhaarApi(params as ValidateAadhaarRequestModel)
    }
}