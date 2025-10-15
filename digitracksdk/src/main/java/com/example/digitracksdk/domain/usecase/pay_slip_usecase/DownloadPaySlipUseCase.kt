package com.example.digitracksdk.domain.usecase.pay_slip_usecase

import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModel
import com.example.digitracksdk.domain.repository.pay_slip.PaySlipRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class DownloadPaySlipUseCase constructor(private val paySlipRepository: PaySlipRepository) :
    UseCase<PaySlipDownloadResponseModel, Any?>() {
    override suspend fun run(params: Any?): PaySlipDownloadResponseModel {
        return paySlipRepository.callDownloadPaySlipApi(params as PaySlipDownloadRequestModel)
    }
}