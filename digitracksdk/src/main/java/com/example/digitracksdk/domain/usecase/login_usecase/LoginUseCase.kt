package com.example.digitracksdk.domain.usecase.login_usecase

import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.domain.model.login_model.LoginResponseModel
import com.example.digitracksdk.domain.repository.login_repository.LoginRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class LoginUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<LoginResponseModel, Any?>() {

    override suspend fun run(params: Any?): LoginResponseModel {
        return loginRepository.callLoginApi(params as LoginRequestModel)
    }
}