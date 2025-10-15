package com.example.digitracksdk.domain.usecase.dashboard

import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeRequestModel
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeResponseModel
import com.example.digitracksdk.domain.repository.home_repository.InnovIDCardRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

/**
 * Created by Mo Khurseed Ansari on 03-10-2023.
 */
class QrCodeUseCase constructor(val repository: InnovIDCardRepository) : UseCase<QrCodeResponseModel, Any?>() {
    override suspend fun run(params: Any?): QrCodeResponseModel {
       return repository.callQrCodeApi(params as QrCodeRequestModel)
    }
}