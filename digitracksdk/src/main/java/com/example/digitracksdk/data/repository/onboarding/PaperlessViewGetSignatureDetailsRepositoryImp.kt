package com.example.digitracksdk.data.repository.onboarding

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetSignatureResponseModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureRequestModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureResponseModel
import com.example.digitracksdk.domain.repository.onboarding.signature.PaperlessViewGetSignatureRepository

class PaperlessViewGetSignatureDetailsRepositoryImp
    (private val apiService: ApiService) : PaperlessViewGetSignatureRepository {
    override suspend fun callViewGetSignatureApi(request: InnovIDRequestModel): PaperlessViewGetSignatureResponseModel {
        return apiService.callGetSignatureApi(request)
    }

    override suspend fun callInsertSignatureApi(request: InsertSignatureRequestModel): InsertSignatureResponseModel {
        return apiService.callInsertSignatureApi(request)
    }
}