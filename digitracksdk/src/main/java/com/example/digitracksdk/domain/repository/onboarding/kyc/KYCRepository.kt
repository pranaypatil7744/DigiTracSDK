package com.example.digitracksdk.domain.repository.onboarding.kyc

import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentListResponseModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListResponseModel

interface KYCRepository {

    suspend fun callDTGetKYCDocsApi(request: EmpIDRequestModel): KycDocumentListResponseModel

    suspend fun callPobDocumentTypeKycApi(request: KycDocumentPendingListRequestModel): KycDocumentPendingListResponseModel

}

