package com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification

import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.GetAadhaarVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarRequestModel
import com.example.digitracksdk.domain.repository.onboarding.aadhar_verification.AadhaarVerificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class GetAadhaarVerificationDetailsUseCase constructor(private val aadhaarVerificationRepository: AadhaarVerificationRepository) :
    UseCase<GetAadhaarVerificationDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetAadhaarVerificationDetailsResponseModel {
        return aadhaarVerificationRepository.callGetAadhaarVerificationDetailsApi(params as ValidateAadhaarRequestModel)
    }
}