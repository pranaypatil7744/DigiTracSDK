package com.example.digitracksdk.data.repository.pay_slip_repo_imp

import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearsResponseModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusRequestModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModel
import com.example.digitracksdk.domain.repository.pay_slip.PaySlipRepository

class PaySlipRepositoryImp(private val apiServiceCandidate: ApiService, private val apiServiceNormal: ApiService):
    PaySlipRepository {
    override suspend fun callPaySlipMonthYearApi(request: PaySlipMonthYearRequestModel): PaySlipMonthYearsResponseModel {
        return apiServiceCandidate.callPaySlipMonthYearApi(request)
    }

    override suspend fun callSalaryReleaseStatusApi(request: SalaryReleaseStatusRequestModel): SalaryReleaseStatusResponseModel {
        return apiServiceNormal.callSalaryReleaseStatusApi(request)
    }

    override suspend fun callDownloadPaySlipApi(request: PaySlipDownloadRequestModel): PaySlipDownloadResponseModel {
        return apiServiceCandidate.callDownloadPaySlipApi(request.InnovID,request.Month,request.Year)
    }

    override suspend fun callPaySlipDownloadApiNew(request: PaySlipDownloadRequestModelNew): PaySlipDownloadResponseModelNew {
        return apiServiceNormal.callPaySlipDownloadApiNew(request)
    }
}