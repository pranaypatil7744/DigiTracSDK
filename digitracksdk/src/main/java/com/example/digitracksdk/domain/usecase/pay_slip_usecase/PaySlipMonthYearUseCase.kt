package com.example.digitracksdk.domain.usecase.pay_slip_usecase

import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearsResponseModel
import com.example.digitracksdk.domain.repository.pay_slip.PaySlipRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class PaySlipMonthYearUseCase constructor(private val paySlipRepository: PaySlipRepository) :
    UseCase<PaySlipMonthYearsResponseModel, Any?>() {
    override suspend fun run(params: Any?): PaySlipMonthYearsResponseModel {
        return paySlipRepository.callPaySlipMonthYearApi(params as PaySlipMonthYearRequestModel)
    }
}