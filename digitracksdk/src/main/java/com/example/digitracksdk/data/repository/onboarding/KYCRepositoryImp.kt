package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentListResponseModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListResponseModel
import com.example.digitracksdk.domain.repository.onboarding.kyc.KYCRepository

class KYCRepositoryImp
    (private val apiService: ApiService) : KYCRepository {

    override suspend fun callDTGetKYCDocsApi(request: EmpIDRequestModel): KycDocumentListResponseModel {
        return apiService.callDTGetKYCDocsApi(request)
    }

    override suspend fun callPobDocumentTypeKycApi(request: KycDocumentPendingListRequestModel): KycDocumentPendingListResponseModel {
        return apiService.callKycPendingDocListApi(request)
    }

}