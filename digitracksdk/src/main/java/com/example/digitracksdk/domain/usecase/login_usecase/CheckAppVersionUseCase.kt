package com.example.digitracksdk.domain.usecase.login_usecase

import com.example.digitracksdk.domain.model.login_model.CheckVersionResponseModel
import com.example.digitracksdk.domain.repository.login_repository.LoginRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CheckAppVersionUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<CheckVersionResponseModel, Any?>() {
    override suspend fun run(params: Any?): CheckVersionResponseModel {
        return loginRepository.callCheckAppVersionApi()
    }
}