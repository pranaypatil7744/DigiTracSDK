package com.example.digitracksdk.domain.usecase.pay_slip_usecase

import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModelNew
import com.example.digitracksdk.domain.repository.pay_slip.PaySlipRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PaySlipDownloadNewUseCase constructor(private val paySlipRepository: PaySlipRepository) :
    UseCase<PaySlipDownloadResponseModelNew, Any?>() {
    override suspend fun run(params: Any?): PaySlipDownloadResponseModelNew {
        return paySlipRepository.callPaySlipDownloadApiNew(params as PaySlipDownloadRequestModelNew)
    }
}