package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeRequestModel
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeResponseModel
import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.repository.home_repository.InnovIDCardRepository

class InnovIDCardRepositoryImp(var apiService: ApiService): InnovIDCardRepository {

    override suspend fun callInnovIDCardApi(request: InnovIDCardRequestModel): InnovIDCardResponseModel {
        return apiService.callCandidateInfo(request)
    }

    override suspend fun callQrCodeApi(request: QrCodeRequestModel): QrCodeResponseModel {
      return  apiService.callQrCodeApi(GnetAssociateID = request.GNETAssociateID)
    }

}