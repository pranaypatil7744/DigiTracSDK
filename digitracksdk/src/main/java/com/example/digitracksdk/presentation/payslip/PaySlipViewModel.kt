package com.example.digitracksdk.presentation.payslip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearsResponseModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusRequestModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusResponseModel
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModelNew
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.DownloadPaySlipUseCase
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.PaySlipDownloadNewUseCase
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.PaySlipMonthYearUseCase
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.SalaryReleaseStatusUseCase
import kotlinx.coroutines.cancel

class PaySlipViewModel constructor(
    private val downloadPaySlipUseCase: DownloadPaySlipUseCase,
    private val paySlipMonthYearUseCase: PaySlipMonthYearUseCase,
    private val salaryReleaseStatusUseCase: SalaryReleaseStatusUseCase,
    private val paySlipDownloadNew: PaySlipDownloadNewUseCase
) : ViewModel() {

    val paySlipDownloadResponseData = MutableLiveData<PaySlipDownloadResponseModel>()
    val paySlipMonthYearsResponseData = MutableLiveData<PaySlipMonthYearsResponseModel>()
    val salaryReleaseStatusResponseData = MutableLiveData<SalaryReleaseStatusResponseModel>()
    val paySlipDownloadNewResponseData = MutableLiveData<PaySlipDownloadResponseModelNew>()
    val messageData = MutableLiveData<String>()
    val showProgressBar = MutableLiveData<Boolean>()


    fun callSalaryReleaseStatusApi(request: SalaryReleaseStatusRequestModel){
        showProgressBar.value =true
        salaryReleaseStatusUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<SalaryReleaseStatusResponseModel> {
            override fun onSuccess(result: SalaryReleaseStatusResponseModel) {
                showProgressBar.value = false
                salaryReleaseStatusResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }

    fun callPaySlipDownloadNewApi(request: PaySlipDownloadRequestModelNew){
        showProgressBar.value =true
        paySlipDownloadNew.invoke(viewModelScope,request,object :
            UseCaseResponse<PaySlipDownloadResponseModelNew> {
            override fun onSuccess(result: PaySlipDownloadResponseModelNew) {
                showProgressBar.value = false
                paySlipDownloadNewResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }

    fun callPaySlipMonthYearApi(request: PaySlipMonthYearRequestModel){
        showProgressBar.value =true
        paySlipMonthYearUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<PaySlipMonthYearsResponseModel> {
            override fun onSuccess(result: PaySlipMonthYearsResponseModel) {
                showProgressBar.value = false
                paySlipMonthYearsResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }

    fun callPaySlipDownloadApi(request: PaySlipDownloadRequestModel){
        showProgressBar.value =true
        downloadPaySlipUseCase.invoke(viewModelScope,request,object :
            UseCaseResponse<PaySlipDownloadResponseModel> {
            override fun onSuccess(result: PaySlipDownloadResponseModel) {
                showProgressBar.value = false
                paySlipDownloadResponseData.value = result
            }

            override fun onError(apiError: ApiError?) {
                showProgressBar.value = false
                messageData.value = apiError?.message.toString()
            }

        })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}