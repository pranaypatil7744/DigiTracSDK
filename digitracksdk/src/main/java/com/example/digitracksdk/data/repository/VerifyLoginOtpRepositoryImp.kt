package com.example.digitracksdk.data.repository

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyRequestModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyResponseModel
import com.example.digitracksdk.domain.repository.verify_login_otp_repository.VerifyLoginOtpRepository

class VerifyLoginOtpRepositoryImp(val apiService: ApiService): VerifyLoginOtpRepository {

    override suspend fun callVerifyLoginOtpApi(request: LoginOtpVerifyRequestModel): LoginOtpVerifyResponseModel {
        return apiService.callVerifyLoginOtp(request)
    }
}