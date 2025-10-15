package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.GetAadhaarVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.repository.onboarding.aadhar_verification.AadhaarVerificationRepository

class AadhaarVerificationRepositoryImp(
    private val apiServiceAssociate: ApiService,
    private val apiServiceNormal: ApiService
) :
    AadhaarVerificationRepository {
    override suspend fun callAadhaarVerificationSendOtpApi(request: AadhaarVerificationSendOtpRequestModel): AadhaarVerificationSendOtpResponseModel {
        return apiServiceAssociate.callAadhaarVerificationSendOtpApi(request)
    }

    override suspend fun callAadhaarVerificationOtpValidationApi(request: AadhaarVerificationOtpValidationRequestModel): AadhaarVerificationOtpValidationResponseModel {
        return apiServiceAssociate.callAadhaarVerificationOtpValidationApi(request)
    }

    override suspend fun callValidateAadhaarApi(request: ValidateAadhaarRequestModel): ValidateAadhaarResponseModel {
        return apiServiceNormal.callValidateAadhaarApi(request)
    }

    override suspend fun callGetAadhaarVerificationDetailsApi(request: ValidateAadhaarRequestModel): GetAadhaarVerificationDetailsResponseModel {
        return apiServiceNormal.callGetAadhaarVerificationDetailsApi(request)
    }
}