package com.example.digitracksdk.domain.repository.onboarding.signature

import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetSignatureResponseModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureRequestModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureResponseModel

interface PaperlessViewGetSignatureRepository {

    suspend fun callViewGetSignatureApi(request: InnovIDRequestModel): PaperlessViewGetSignatureResponseModel

    suspend fun callInsertSignatureApi(request: InsertSignatureRequestModel): InsertSignatureResponseModel

}

