package com.example.digitracksdk.domain.usecase.login_usecase

import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyRequestModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyResponseModel
import com.example.digitracksdk.domain.repository.verify_login_otp_repository.VerifyLoginOtpRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class VerifyLoginOtpUseCase constructor(private val verifyLoginOtpRepository: VerifyLoginOtpRepository) :
    UseCase<LoginOtpVerifyResponseModel, Any?>() {
    override suspend fun run(params: Any?): LoginOtpVerifyResponseModel {
        return verifyLoginOtpRepository.callVerifyLoginOtpApi(params as LoginOtpVerifyRequestModel)
    }

}