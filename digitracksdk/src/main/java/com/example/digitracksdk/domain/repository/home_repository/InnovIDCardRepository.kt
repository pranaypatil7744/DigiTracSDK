package com.example.digitracksdk.domain.repository.home_repository

import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeRequestModel
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeResponseModel
import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel

interface InnovIDCardRepository {
    suspend fun callInnovIDCardApi(request: InnovIDCardRequestModel): InnovIDCardResponseModel
    suspend fun callQrCodeApi(request: QrCodeRequestModel): QrCodeResponseModel
}