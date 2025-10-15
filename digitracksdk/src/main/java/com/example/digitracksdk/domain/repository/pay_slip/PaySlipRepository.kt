package com.example.digitracksdk.domain.repository.pay_slip

import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearsResponseModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusRequestModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusResponseModel

interface PaySlipRepository {

    suspend fun callPaySlipMonthYearApi(request: PaySlipMonthYearRequestModel): PaySlipMonthYearsResponseModel

    suspend fun callSalaryReleaseStatusApi(request: SalaryReleaseStatusRequestModel): SalaryReleaseStatusResponseModel

    suspend fun callDownloadPaySlipApi(request: PaySlipDownloadRequestModel): PaySlipDownloadResponseModel

    suspend fun callPaySlipDownloadApiNew(request: PaySlipDownloadRequestModelNew): PaySlipDownloadResponseModelNew

}