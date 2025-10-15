package com.example.digitracksdk.domain.repository.verify_login_otp_repository

import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyRequestModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyResponseModel

interface VerifyLoginOtpRepository {

    suspend fun callVerifyLoginOtpApi(request: LoginOtpVerifyRequestModel): LoginOtpVerifyResponseModel
}