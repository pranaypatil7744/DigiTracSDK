package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationResponseModel
import com.example.digitracksdk.domain.repository.onboarding.pan_verification.PanVerificationRepository

class PanVerificationRepositoryImp(
    private val apiServiceAssociate: ApiService,
    private val apiServiceNormal: ApiService
) :
    PanVerificationRepository {

    override suspend fun callPanVerificationApi(request: PanCardVerificationRequestModel): PanCardVerificationResponseModel {
        return apiServiceAssociate.callPanVerificationApi(request)
    }

    override suspend fun callPanVerificationDetailsApi(request: GetPanCardVerificationDetailsRequestModel): GetPanCardVerificationDetailsResponseModel {
        return apiServiceNormal.callPanVerificationDetailsApi(request)
    }
}