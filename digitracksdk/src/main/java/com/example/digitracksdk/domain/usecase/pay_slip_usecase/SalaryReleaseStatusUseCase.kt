package com.example.digitracksdk.domain.usecase.pay_slip_usecase

import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusRequestModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusResponseModel
import com.example.digitracksdk.domain.repository.pay_slip.PaySlipRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class SalaryReleaseStatusUseCase constructor(private val paySlipRepository: PaySlipRepository) :
    UseCase<SalaryReleaseStatusResponseModel, Any?>() {
    override suspend fun run(params: Any?): SalaryReleaseStatusResponseModel {
        return paySlipRepository.callSalaryReleaseStatusApi(params as SalaryReleaseStatusRequestModel)
    }
}